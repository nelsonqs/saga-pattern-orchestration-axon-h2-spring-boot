package com.progressivecoder.paymentmanagement.paymentservice.queryModel.finalView.payment.repositories;

import com.progressivecoder.paymentmanagement.paymentservice.queryModel.finalView.payment.InvoiceEntity;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<InvoiceEntity, String> {

}
