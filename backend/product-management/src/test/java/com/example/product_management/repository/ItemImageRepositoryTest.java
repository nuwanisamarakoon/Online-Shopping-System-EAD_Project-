package com.example.product_management.repository;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.example.product_management.model.Category;
import com.example.product_management.model.Item;
import com.example.product_management.model.ItemImage;

@DataJpaTest
public class ItemImageRepositoryTest {
    @Autowired
    private ItemImageRepository underTestItemImageRepository;

    @Autowired
    private ItemRepository underTestItemRepository;

    @Autowired
    private CategoryRepository underTestCategoryRepository;

    private Item underTestItem;

    private Category underTestCategory;

    private ItemImage underTestItemImage;

    @BeforeEach
    void setup() {
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

        underTestItemImage = new ItemImage();
        underTestItemImage.setId(3);
        underTestItemImage.setImageUrl("http://www.example.com");
        underTestItemImage.setItem(underTestItem);

        underTestCategoryRepository.save(underTestCategory);
        underTestItemRepository.save(underTestItem);
        underTestItemImageRepository.save(underTestItemImage);
    }

    @Test
    public void testFindByItemId() {
        List<ItemImage> foundItemImage = underTestItemImageRepository.findByItemId(underTestItem.getId());

        assertFalse(foundItemImage.isEmpty(), "Item Images were found for the given ID");
        assertEquals(1, foundItemImage.size(), "Only one item Image should be found");

    }
}
