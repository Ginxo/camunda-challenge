package com.camunda.interview.emingora.javachallenge.core;

import com.camunda.interview.emingora.javachallenge.util.UrlRequestUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BPMQueryService {

    private final Logger logger = LoggerFactory.getLogger(BPMQueryService.class);

    public List<String> queryBpmnNodes(final String bpmUrl, final String startNodeId, final String endNodeId) {
        return queryBpmnNodes(fetchBpmnFromURL(bpmUrl), startNodeId, endNodeId);
    }

    public List<String> queryBpmnNodes(final BpmnModelInstance bpmnModelInstance, final String startNodeId, final String endNodeId) {
        if (startNodeId.equals(endNodeId)) {
            logger.warn(String.format("Nodes are equal with value %s. Should we accept this case or throw an error? I'm returning empty list since there's no route between them and don't want to waste processing time...", startNodeId));
            return new ArrayList<>();
        }

        final ModelElementInstance startNode = bpmnModelInstance.getModelElementById(startNodeId);
        final ModelElementInstance endNode = bpmnModelInstance.getModelElementById(endNodeId);
        if (startNode == null || endNode == null) {
            throw new RuntimeException(String.format("Either %s and/or %s nodes do not exist in BPMN mode", startNodeId, endNodeId));
        }
        if (!(startNode instanceof FlowNode) || !(endNode instanceof FlowNode)) {
            throw new RuntimeException(String.format("Either %s and/or %s nodes are not Flow Nodes", startNodeId, endNodeId));
        }

        return composePathBetweenNodes((FlowNode) startNode, (FlowNode) endNode);
    }

    private List<String> composePathBetweenNodes(final FlowNode startNode, final FlowNode endNode, String... alreadyVisitedNodes) {
        List<FlowNode> succeedingNodes = startNode.getSucceedingNodes().list().stream().filter(n -> !Stream.of(alreadyVisitedNodes).anyMatch(n.getId()::equals)).collect(Collectors.toList());
        if (succeedingNodes.size() == 0) {
            return new ArrayList<>();
        }

        final String foundSuccession = succeedingNodes.stream().map(node -> node.getId()).filter(nodeId -> nodeId.equals(endNode.getId())).findFirst().orElse(null);

        if (foundSuccession != null) {
            return Arrays.asList(startNode.getId(), endNode.getId());
        } else {
            for (FlowNode node : succeedingNodes) {
                final List<String> nodes = composePathBetweenNodes(node, endNode, ArrayUtils.add(alreadyVisitedNodes, node.getId()));
                if (nodes.size() > 0) {
                    return Stream.concat(Arrays.asList(startNode.getId()).stream(), nodes.stream()).collect(Collectors.toList());
                }
            }
            return new ArrayList<>();
        }
    }

    private BpmnModelInstance fetchBpmnFromURL(final String url) {
        final JSONObject bbpmJsonObject = UrlRequestUtil.fetchUrl(url);
        return Bpmn.readModelFromStream(new ByteArrayInputStream(((String) bbpmJsonObject.get("bpmn20Xml")).getBytes()));
    }
}
