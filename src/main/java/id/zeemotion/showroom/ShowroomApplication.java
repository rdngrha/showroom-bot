package id.zeemotion.showroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import id.zeemotion.showroom.service.ShowroomService;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ShowroomApplication implements CommandLineRunner
{
    @Autowired private Environment env;
    @Autowired private ShowroomService showroomService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Service is ready...");
        while (true) {
            try {
                showroomService.getShowroomData();
                Thread.sleep(Long.valueOf(env.getProperty("app.delay")));
            } catch (Exception e) {
                log.error(e.getMessage());
                Thread.sleep(Long.valueOf(env.getProperty("app.delay")));
            }
        }
    }
}
