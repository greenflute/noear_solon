package webapp;

import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author noear 2021/9/9 created
 */
@Configuration
public class Config {
    @Bean
    public Map<String, Object> map() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);

        return map;
    }

//    @Bean
//    public CacheService cache(){
//        return new LocalCacheService();
//    }

//    @Init
//    public void xxx(){
//        throw new RuntimeException("xxxxxx");
//    }
}
