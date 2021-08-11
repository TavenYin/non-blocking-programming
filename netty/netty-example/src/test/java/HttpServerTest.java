import org.junit.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class HttpServerTest {

    @Test
    public void httpTest() throws InterruptedException {
        var httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:6665"))
                .header("forward", "true")
                .timeout(Duration.ofSeconds(10))
                .build();

        while (true) {
            CountDownLatch countDownLatch = new CountDownLatch(32);
            System.out.println("start");
            // netty 默认创建 cpu core * 2 的事件线程数，创建 cpu core * 4 的测试请求，让事件线程忙碌起来
            for (int i = 0; i < 32; i++) {
                Date date = new Date();
                CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
                future.thenAccept(stringHttpResponse -> {
                    System.out.println("date1:"+date+ "date2"+ new Date()+", response:"+stringHttpResponse.body());
                    countDownLatch.countDown();
                });
            }
            countDownLatch.await();
            break;
        }

    }

}
