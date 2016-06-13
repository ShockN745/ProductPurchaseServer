package com.professionalbeginner.domain.core;

import com.google.common.testing.EqualsTester;
import com.professionalbeginner.domain.core.validator.PurchaseValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by Florian on 09/06/16.
 */
public class PurchaseTest {

    DetailsFactory detailsFactory;
    Details details;
    Details otherDetails;

    PurchaseFactory factory;

    @Mock
    PurchaseValidator validator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        detailsFactory = new DetailsFactory();
        factory = new PurchaseFactory(validator);

        details = detailsFactory.make(1, "description 1", 1, 1);
        otherDetails = detailsFactory.make(2, "description 2", 2, 2);
    }

    @Test
    public void testEquality() throws Exception {
        new EqualsTester()
                .addEqualityGroup(
                        factory.make(1, "type", LocalDateTime.MAX, details),
                        factory.make(1, "type", LocalDateTime.MAX, details)
                )
                .addEqualityGroup(
                        factory.make(2, "type", LocalDateTime.MAX, otherDetails),
                        factory.make(2, "type", LocalDateTime.MAX, otherDetails)
                )
                .addEqualityGroup(
                        factory.make(1, "other", LocalDateTime.MAX, details),
                        factory.make(1, "other", LocalDateTime.MAX, details)
                )
                .addEqualityGroup(
                        factory.make(1, "type", LocalDateTime.MIN, details),
                        factory.make(1, "type", LocalDateTime.MIN, details)
                )
                .addEqualityGroup(
                        Purchase.NULL,
                        Purchase.NULL
                )
                .testEquals();
    }

    @Test
    public void nullType_replaceWithEmpty() throws Exception {
        Purchase purchase = factory.make(1, null, LocalDateTime.MAX, details);
        assertEquals("", purchase.getProductType());
    }

    @Test
    public void nullExpire_replaceWithMIN() throws Exception {
        Purchase purchase = factory.make(1, "type", null, details);
        assertEquals(LocalDateTime.MIN, purchase.getExpires());
    }

    @Test
    public void nullDetails_replaceWithNullObject() throws Exception {
        Purchase purchase = factory.make(1, "type", LocalDateTime.MAX, null);
        assertEquals(Details.NULL, purchase.getPurchaseDetails());
    }

    @Test
    public void verifyValidity_callsValidator() throws Exception {
        Purchase purchase = factory.make(1, "type", LocalDateTime.MAX, null);
        LocalDateTime currentTime = LocalDateTime.of(2016, 1, 1, 1, 1);

        purchase.validate(currentTime);

        verify(validator).validForCurrentTime(eq(purchase), eq(currentTime));
    }

    @Test
    public void detailId_mustBeSimilarTo_PurchaseId() throws Exception {
        try {
            factory.make(1, "type", LocalDateTime.MIN, detailsFactory.make(2, "", 1, 1));
            fail("Different PurchaseId and DetailsId should throw exception");
        } catch (RuntimeException e) {
            //expected do nothing
        }

        //Do not throw exception
        try {
            factory.make(2, "asd", LocalDateTime.MIN, Details.NULL);
        } catch (RuntimeException e) {
            fail("Should not check Id on NULL detail");
        }

    }
}