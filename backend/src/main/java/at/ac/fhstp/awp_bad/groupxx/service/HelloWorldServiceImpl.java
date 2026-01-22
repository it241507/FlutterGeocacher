package at.ac.fhstp.awp_bad.groupxx.service;

import at.ac.fhstp.awp_bad.groupxx.dtos.request.HelloWorldMessageDto;
import at.ac.fhstp.awp_bad.groupxx.entities.HelloWorldMessage;
import at.ac.fhstp.awp_bad.groupxx.repository.HelloWorldRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class HelloWorldServiceImpl implements HelloWorldService {

    private final HelloWorldRepository helloWorldRepository;
    public HelloWorldServiceImpl(HelloWorldRepository helloWorldRepository) {
        this.helloWorldRepository = helloWorldRepository;
    }

    @Override
    public void save(HelloWorldMessageDto helloWorldMessageDto) {
        HelloWorldMessage helloWorldMessage = new HelloWorldMessage();
        helloWorldMessage.setMessage(helloWorldMessageDto.getMessageDE());
        helloWorldMessage.setMessage(helloWorldMessageDto.getMessageEN());
        helloWorldMessage.setTimestamp(Instant.now());
        helloWorldRepository.save(helloWorldMessage);
    }

}
