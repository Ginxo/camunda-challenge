package com.camunda.interview.emingora.javachallenge.rest;

import com.camunda.interview.emingora.javachallenge.core.BPMQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BPMQueryRestService {

    // this is "wrong" (can be improved) but this is the use case
    private final String BPM_URL = "https://n35ro2ic4d.execute-api.eu-central-1.amazonaws.com/prod/engine-rest/process-definition/key/invoice/xml";

    @Autowired
    private BPMQueryService bpmQueryService;

    @GetMapping("/query/bpmn")
    @ResponseBody
    public List<String> queryBpmnNodes(@RequestParam final String startNodeId, @RequestParam final String endNodeId) {
        return bpmQueryService.queryBpmnNodes(BPM_URL, startNodeId, endNodeId);
    }
}
