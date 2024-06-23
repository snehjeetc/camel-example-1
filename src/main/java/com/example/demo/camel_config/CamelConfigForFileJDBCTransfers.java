package com.example.demo.camel_config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfigForFileJDBCTransfers extends RouteBuilder{


    @Autowired
    DataSource dataSource; 

    @Override
    public void configure() throws Exception {
        
        // getContext().getCom 

        onException(Throwable.class).log("${body}"); 

        from("file:{{src.fileIN-path}}?noop=true&delete=true")
        .choice()
            .when(header(Exchange.FILE_NAME).startsWith("new"))
            .to("file:{{src.fileOUT-path}}")
            .when(header(Exchange.FILE_NAME).startsWith("jdbc"))
                .process(exchange ->  {
                        var body = exchange.getIn().getBody(String.class); 
                        List<Map<String, Object>> map = Stream.of(body.split("\r\n"))
                        .map(line -> { 
                            Map<String, Object> m = new HashMap<>(); 
                            String[] cArr = line.split(","); 
                            for(int i = 0; i < cArr.length; i++)
                                m.put("c" +(i+1), cArr[i]); 
                            return m; 
                        }).collect(Collectors.toList()); 
                        exchange.getIn().setBody(map);
                    }).log("{body}")
                    .to("sql:INSERT into student(rollnum, name, class, section) VALUES (:#c1, :#c2, :#c3, :#c4)?batch=true");

                    from("direct:fetchAllStudent")
                    .to("sql:SELECT * from student")
                    .wireTap("direct:processfilereq")
                    // .process(exchange -> { 
                    //   var body = exchange.getIn().getBody();
                    //   System.out.println(body.getClass()); 
                    //   System.out.println(body.toString());    
                    // })
                    // .unmarshal().json().
                    // to("file:{{src.fileOUT-path}}?fileName=${date:now:ddMMyyyy-hh:mm:ss}.txt")
                    .end();

                    from("direct:processfilereq")
                    .process(exchange -> { 
                        List<Map> body = exchange.getIn().getBody(List.class); 
                        StringBuilder sb = new StringBuilder(); 
                        sb.append("rollnum,name,class,section\n");
                        for(var record : body){ 
                            sb.append(record.get("rollnum")).append(","); 
                            sb.append(record.get("name")).append(","); 
                            sb.append(record.get("class")).append(","); 
                            sb.append(record.get("section")); 
                            sb.append("\n"); 
                        }
                        exchange.getIn().setBody(sb.toString());
                    }).to("file:{{src.fileOUT-path}}?fileName=${date:now:ddMMyyyy-hhmmss}.txt");

    }

    
}
