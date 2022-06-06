import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;


public class kafkaTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .inStreamingMode()
                .useBlinkPlanner()
                .build();

        TableEnvironment tableEnv = TableEnvironment.create(settings);

        // 1. 在创建表的DDL中直接定义时间属性
//        String createDDL = "CREATE TABLE clickTable (" +
//                " user_name STRING, " +
//                " url STRING, " +
//                " ts DOUBLE " +
//                ") WITH (" +
//                " 'connector' = 'filesystem', " +
//                " 'path' = 'D:\\kafkaTest\\src\\main\\kafka\\clicks.csv', " +
//                " 'format' =  'csv' " +
//                ")";
        String createDDL = "CREATE TABLE clickTable (" +
                " user_name STRING, " +
                " url STRING, " +
                " ts DOUBLE " +
                ") WITH (" +
                " 'connector' = 'kafka', " +
                " 'topic' = 'sensor', "+
                " 'properties.bootstrap.servers' = '192.168.163.133:9092', " +
                " 'properties.group.id' = 'testGroup', " +
                " 'scan.startup.mode' = 'latest-offset', " +
                " 'format' =  'csv' " +
                ")";
//        tableEnv.executeSql(createDDL);
        tableEnv.sqlUpdate(createDDL);
        Table table = tableEnv.sqlQuery("select user_name,url,ts from clickTable");
        tableEnv.createTemporaryView("temp_table",table);
        Table table1 = tableEnv.sqlQuery("select * from temp_table");
//        table1.execute();

//        String createPrintOutDDL = "CREATE TABLE printOutTable (" +
//                " user_name STRING, " +
//                " url STRING " +
//                ") WITH (" +
//                " 'connector' = 'print' " +
//                ")";
        String createPrintOutDDL = "CREATE TABLE printOutTable (" +
                " user_name STRING, " +
                " url STRING, " +
                " ts DOUBLE " +
                ") WITH (" +
                " 'connector' = 'jdbc', " +
                " 'url' = 'jdbc:mysql://localhost:3306/test', " +
                " 'username' = 'root', " +
                " 'password' = '123456', " +
                " 'table-name' = 'users' " +
                ")";
        tableEnv.executeSql(createPrintOutDDL);
        table1.executeInsert("printOutTable").print();


//        Properties properties = new Properties();
//        properties.setProperty("bootstrap.servers", "192.168.163.133:9092");
//        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        properties.setProperty("auto.offset.reset", "latest");
//
//        // 从文件读取数据
//        DataStream<String> dataStream = env.addSource( new FlinkKafkaConsumer011<String>("sensor", new SimpleStringSchema(), properties));
//        // 打印输出
//        dataStream.print();

//        env.execute();
    }
}
