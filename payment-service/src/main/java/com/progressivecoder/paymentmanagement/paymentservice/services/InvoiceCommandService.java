package com.progressivecoder.paymentmanagement.paymentservice.services;

import com.progressivecoder.paymentmanagement.paymentservice.commands.InvoiceCreateDTO;

import java.util.concurrent.CompletableFuture;

public interface InvoiceCommandService {

    public CompletableFuture<String> invoiceOrder(InvoiceCreateDTO orderCreateDTO);

}
