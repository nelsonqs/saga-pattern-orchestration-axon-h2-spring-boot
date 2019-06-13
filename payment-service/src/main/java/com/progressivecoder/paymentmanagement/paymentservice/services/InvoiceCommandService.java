package com.progressivecoder.paymentmanagement.paymentservice.services;

import com.progressivecoder.paymentmanagement.paymentservice.commands.InvoiceCreateDTO;
import com.progressivecoder.paymentmanagement.paymentservice.commands.RejectedInvoiceDTO;

import java.util.concurrent.CompletableFuture;

public interface InvoiceCommandService {

    CompletableFuture<String> invoiceOrder(InvoiceCreateDTO orderCreateDTO);

    CompletableFuture<String> rejectedOrder(RejectedInvoiceDTO orderCreateDTO);

}
