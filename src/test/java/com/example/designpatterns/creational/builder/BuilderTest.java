package com.example.designpatterns.creational.builder;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Asserts the guarantee Builder buys: an invalid {@code OrderRequest} cannot be
 * constructed at all, so no downstream code needs to defend against one.
 */
class BuilderTest {

    @Test
    void buildsAValidOrderAndComputesItsTotal() {
        OrderRequest order = OrderRequest.builder()
                .customer("Ada Lovelace")
                .addLine("SKU-1", 2, new BigDecimal("10.00"))
                .addLine("SKU-2", 1, new BigDecimal("5.50"))
                .shipTo("12 Analytical Way")
                .expedited(true)
                .build();

        assertThat(order.customer()).isEqualTo("Ada Lovelace");
        assertThat(order.lines()).hasSize(2);
        assertThat(order.total()).isEqualByComparingTo("25.50");
        assertThat(order.expedited()).isTrue();
    }

    @Test
    void refusesToBuildAnOrderWithNoLines() {
        assertThatThrownBy(() -> OrderRequest.builder()
                .customer("Ada")
                .shipTo("somewhere")
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("at least one line");
    }

    @Test
    void refusesToBuildAnOrderWithoutACustomer() {
        assertThatThrownBy(() -> OrderRequest.builder()
                .addLine("SKU-1", 1, new BigDecimal("1.00"))
                .shipTo("somewhere")
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("customer");
    }

    @Test
    void rejectsNonPositiveQuantityAtTheCallSiteNotAtBuildTime() {
        assertThatThrownBy(() -> OrderRequest.builder().addLine("SKU-1", 0, BigDecimal.ONE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantity");
    }

    @Test
    void theBuiltObjectExposesNoMutators() {
        boolean anySetter = Arrays.stream(OrderRequest.class.getMethods())
                .anyMatch(method -> method.getName().startsWith("set"));

        assertThat(anySetter).isFalse();
    }

    @Test
    void theLineListCannotBeMutatedThroughTheBuiltObject() {
        OrderRequest order = OrderRequest.builder()
                .customer("Ada")
                .addLine("SKU-1", 1, BigDecimal.ONE)
                .shipTo("somewhere")
                .build();

        assertThatThrownBy(() -> order.lines().add(new OrderLine("SKU-9", 1, BigDecimal.TEN)))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
