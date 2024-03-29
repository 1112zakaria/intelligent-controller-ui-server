package leo.satellite.capstoneuiserver.controller;

import leo.satellite.capstoneuiserver.dto.matlab.SatTestDto;
import leo.satellite.capstoneuiserver.entity.SatTestRowEntity;
import leo.satellite.capstoneuiserver.entity.SatTestTableEntity;
import leo.satellite.capstoneuiserver.mapper.SatTestMapper;
import leo.satellite.capstoneuiserver.repository.SatTestRowRepository;
import leo.satellite.capstoneuiserver.repository.SatTestTableRepository;
import leo.satellite.capstoneuiserver.services.SatTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class SatTestController {

    @Value("${matlab-sim.satTest}")
    private String satTestEndpoint;
    private final SatTestService service;
    private final SatTestMapper mapper;

    @GetMapping("/run")
    public String helloWorld() {
        return "hello world";
    }

    @GetMapping("/run/satTest")
    public List<SatTestDto> runSatTest() {
        RestTemplate restTemplate = new RestTemplate();
        String uri = satTestEndpoint;
        ResponseEntity<List<SatTestDto>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<SatTestDto>>() {});
        List<SatTestDto> responseArray = responseEntity.getBody();
        log.info(responseArray.toString());
        List<SatTestRowEntity> entities = mapper.toSatTestEntity(responseArray);
        log.info(entities.toString());

        // FIXME: add conditional to check if user is not anonymous? Maybe 0 can be anon?
        service.updateUserTable("guest", entities);
        return responseArray;
    }
}
