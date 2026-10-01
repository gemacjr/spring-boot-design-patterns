package com.example.designpatterns.structural.composite;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Asserts the guarantee Composite buys: a client written against the component
 * type behaves identically whether it is handed one leaf or a whole subtree, so
 * no client ever needs an "is this a group?" branch.
 */
class CompositeTest {

    private OrgNode company() {
        Department engineering = new Department("Engineering")
                .add(new Employee("Ada", new BigDecimal("9000")))
                .add(new Employee("Grace", new BigDecimal("9500")))
                .add(new Department("Platform")
                        .add(new Employee("Linus", new BigDecimal("8800")))
                        .add(new Employee("Margaret", new BigDecimal("9200"))));

        return new Department("Acme")
                .add(engineering)
                .add(new Department("Sales").add(new Employee("Don", new BigDecimal("7000"))));
    }

    @Test
    void aggregatesAcrossTheWholeSubtree() {
        OrgNode acme = company();

        assertThat(acme.headcount()).isEqualTo(5);
        assertThat(acme.monthlyCost()).isEqualByComparingTo("43500");
    }

    @Test
    void aLeafAndACompositeAnswerTheSameQuestions() {
        OrgNode leaf = new Employee("Ada", new BigDecimal("9000"));
        OrgNode composite = company();

        for (OrgNode node : java.util.List.of(leaf, composite)) {
            assertThat(node.name()).isNotBlank();
            assertThat(node.headcount()).isPositive();
            assertThat(node.monthlyCost()).isGreaterThan(BigDecimal.ZERO);
            assertThat(node.render(0)).isNotBlank();
        }
    }

    @Test
    void aLeafIsSimplyASubtreeOfSizeOne() {
        OrgNode ada = new Employee("Ada", new BigDecimal("9000"));

        assertThat(ada.headcount()).isEqualTo(1);
        assertThat(ada.monthlyCost()).isEqualByComparingTo("9000");
    }

    @Test
    void anEmptyDepartmentCostsNothingAndEmploysNobody() {
        OrgNode empty = new Department("Skunkworks");

        assertThat(empty.headcount()).isZero();
        assertThat(empty.monthlyCost()).isEqualByComparingTo("0");
    }

    @Test
    void renderingIndentsNestedLevels() {
        String rendered = company().render(0);

        assertThat(rendered).contains("Acme").contains("Engineering").contains("Ada");
        assertThat(rendered.lines().filter(line -> line.contains("Ada")).findFirst().orElseThrow())
                .startsWith("    ");
    }
}
