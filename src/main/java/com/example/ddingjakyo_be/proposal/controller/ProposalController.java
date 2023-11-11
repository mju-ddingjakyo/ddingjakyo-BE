package com.example.ddingjakyo_be.proposal.controller;

import com.example.ddingjakyo_be.common.message.ResponseMessage;
import com.example.ddingjakyo_be.proposal.service.ProposalService;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProposalController {

  private final ProposalService proposalService;

  @PostMapping("/proposal/{teamId}")
  public ResponseEntity<ResponseMessage> proposeMatching() {

  }
}
