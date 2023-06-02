package com.camunda.interview.emingora.javachallenge.core;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class BPMQueryServiceTest {

    private BPMQueryService bpmQueryService = new BPMQueryService();

    private BpmnModelInstance bpmnModelInstance;

    public BPMQueryServiceTest() {
        this.bpmnModelInstance = Bpmn.readModelFromStream(getClass().getClassLoader().getResourceAsStream("bpm.xml"));
    }

    @ParameterizedTest
    @MethodSource("providedTestPaths")
    public void testPaths(String startNodeId, String endNodeId, List<String> expected) {
        // Act
        List<String> result = this.bpmQueryService.queryBpmnNodes(this.bpmnModelInstance, startNodeId, endNodeId);
        // Assert
        Assert.assertEquals(expected, result);
    }

    static Stream<Arguments> providedTestPaths() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.arguments("StartEvent_1", "invoiceNotProcessed", Arrays.asList("StartEvent_1", "assignApprover", "approveInvoice", "invoice_approved", "reviewInvoice", "reviewSuccessful_gw", "invoiceNotProcessed")),
                org.junit.jupiter.params.provider.Arguments.arguments("StartEvent_1", "invoiceProcessed", Arrays.asList("StartEvent_1", "assignApprover", "approveInvoice", "invoice_approved", "prepareBankTransfer", "ServiceTask_1", "invoiceProcessed")),
                org.junit.jupiter.params.provider.Arguments.arguments("prepareBankTransfer", "invoiceNotProcessed", new ArrayList<>()),
                org.junit.jupiter.params.provider.Arguments.arguments("prepareBankTransfer", "prepareBankTransfer", new ArrayList<>()),
                org.junit.jupiter.params.provider.Arguments.arguments("invoiceNotProcessed", "invoiceProcessed", new ArrayList<>()),
                org.junit.jupiter.params.provider.Arguments.arguments("invoiceNotProcessed", "invoiceProcessed", new ArrayList<>()),
                org.junit.jupiter.params.provider.Arguments.arguments("ServiceTask_1", "invoiceProcessed", Arrays.asList("ServiceTask_1", "invoiceProcessed")),
                org.junit.jupiter.params.provider.Arguments.arguments("reviewInvoice", "invoiceProcessed", Arrays.asList("reviewInvoice", "reviewSuccessful_gw", "approveInvoice", "invoice_approved", "prepareBankTransfer", "ServiceTask_1", "invoiceProcessed"))
        );
    }
}
