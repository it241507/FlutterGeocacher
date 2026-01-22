package at.ac.fhstp.awp_bad.groupxx.rest;

import at.ac.fhstp.awp_bad.groupxx.dtos.request.HelloWorldMessageDto;
import at.ac.fhstp.awp_bad.groupxx.dtos.response.HelloWorldDto;
import at.ac.fhstp.awp_bad.groupxx.service.HelloWorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController()
@RequestMapping("/api/helloworld")
public class HelloWorldEndpoint {

    public static final String HI_PUBLIC_MESSAGE = "Hello public world!";
    public static final String HI_PRIVATE_MESSAGE = "Hello private world! (awp_bad_03)";

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldEndpoint.class);

    private final HelloWorldService helloWorldService;

    public HelloWorldEndpoint(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    @GetMapping(path = "/hipublic")
    public String sayHelloPublic() {
        LOG.info("GET called on /hipublic resource");
        return HI_PUBLIC_MESSAGE;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "/hiprivate")
    public String sayHelloPrivate() {
        LOG.info("GET called on /hiprivate resource");
        return HI_PRIVATE_MESSAGE;
    }

    @GetMapping(path = "/hijson")
    public HelloWorldDto hiJson(){
        LOG.info("GET called on /hijson");
        HelloWorldDto helloWorldDto = new HelloWorldDto();
        helloWorldDto.setMessage("Hello JSON!");
        helloWorldDto.setTimestamp(Instant.now());
        return helloWorldDto;
    }

    @PostMapping(path = "/himessage")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void hiMessage(@RequestBody HelloWorldMessageDto helloWorldMessageDto) {
        LOG.info("messageDE: {}", helloWorldMessageDto.getMessageDE());
        LOG.info("messageEN: {}", helloWorldMessageDto.getMessageEN());
        helloWorldService.save(helloWorldMessageDto);
    }
}
