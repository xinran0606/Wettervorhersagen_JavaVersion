import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private Properties properties;

    public ConfigReader(String configFilePath) throws IOException{
        properties = new Properties();  // 我们创建了一个叫 properties 的“容器”。这个容器是用来装配一些我们需要从文件里读取的“钥匙”和“值”。
        FileInputStream fis = new FileInputStream(configFilePath);  // 这行代码打开了一个文件（就是你传给程序的文件路径），文件里面有你存储的配置。FileInputStream 就像是一个可以打开文件的“钥匙”
        properties.load(fis); // 把文件里的内容读到我们之前创建的 properties 容器里。
        fis.close(); // 这行代码会把文件关闭， 关闭文件是为了让其他程序也能使用这个文件，避免它一直占用着不放。
    }

    public String getAPIUrl(){
        return properties.getProperty("API_URL");
    }

    public String getAPIKey(){
        return properties.getProperty("API_Key");
    }
}
