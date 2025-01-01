package com.example.product_management.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.example.product_management.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.product_management.model.Item;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository underTestItemRepository;

    @Autowired
    private CategoryRepository underTestCategoryRepository;

    private Item underTestItem;

    private Category underTestCategory;

    @BeforeEach
    public void setUp() {
        // Arrange: Set up test data
        underTestItem = new Item();
        underTestItem.setId(1);
        underTestItem.setName("T-shirt");
        underTestItem.setDescription("Lorem-ipsum");
        underTestItem.setImageURL("http://www.example.com");
        underTestItem.setQuantity(80);
        underTestItem.setPrice(40.56);
        underTestCategory = new Category();
        underTestCategory.setId(1);
        underTestCategory.setDescription("Lorem_Ipsum_2");
        underTestCategory.setName("Mens");
        underTestCategory.setImageURL("Example_URL");
        underTestItem.setCategory(underTestCategory);

        underTestCategoryRepository.save(underTestCategory);
        underTestItemRepository.save(underTestItem);
    }

    // @Disabled
    @Test
    public void testFindByCategoryId() {
        // Act: Call the repository method
        List<Item> foundItems = underTestItemRepository.findByCategoryId(underTestItem.getCategoryId());

        // Assert: Verify the results
        assertFalse(foundItems.isEmpty(), "Items were found for the given category ID");
        assertEquals(1, foundItems.size(), "Only one item should be found");
        // assertEquals(0, foundItems.size(), "No Items found for given category Id");
    }
}
