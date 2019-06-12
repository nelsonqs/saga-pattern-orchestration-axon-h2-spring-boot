package com.progressivecoder.shippingmanagement.shippingservice.queryModel.finalView.shipping.repositories;

import com.progressivecoder.shippingmanagement.shippingservice.queryModel.finalView.shipping.ShippingEntity;
import org.springframework.data.repository.CrudRepository;

public interface ShippingRepository extends CrudRepository<ShippingEntity, String> {

}
