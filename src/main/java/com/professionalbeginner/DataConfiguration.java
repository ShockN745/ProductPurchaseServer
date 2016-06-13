package com.professionalbeginner;

import com.professionalbeginner.data.detail.jpa.JpaDetailRepository;
import com.professionalbeginner.data.detail.jpa.JpaInMemoryDetailsRepository;
import com.professionalbeginner.data.detail.jpa.JpaDetailMapper;
import com.professionalbeginner.data.purchase.FakePurchaseRepository;
import com.professionalbeginner.data.purchase.jpa.JpaInMemoryPurchaseRepository;
import com.professionalbeginner.data.purchase.jpa.JpaPurchaseMapper;
import com.professionalbeginner.data.purchase.jpa.JpaPurchaseRepository;
import com.professionalbeginner.domain.application.DetailsDTO;
import com.professionalbeginner.domain.application.PurchaseDTO;
import com.professionalbeginner.domain.application.driven.DetailsRepository;
import com.professionalbeginner.domain.application.driven.PurchaseRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataConfiguration {

    @Bean
    public DetailsRepository getDetailsRepository_integration(JpaDetailRepository jpaRepo, JpaDetailMapper jpaMapper) {
        return new JpaInMemoryDetailsRepository(jpaRepo, jpaMapper);
    }

    @Bean
    public PurchaseRepository getPurchaseRepository_integration(JpaPurchaseRepository jpaRepo, JpaPurchaseMapper jpaMapper) {
        return new JpaInMemoryPurchaseRepository(jpaRepo, jpaMapper);
    }

}
