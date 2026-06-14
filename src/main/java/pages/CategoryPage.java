package pages;

import org.openqa.selenium.WebDriver;

public class CategoryPage extends AbsBasePage<CategoryPage> {

    private String categoryPath;

    public CategoryPage(WebDriver driver) {
        super(driver);
    }

    public CategoryPage withCategory(String categorySlug) {
        this.categoryPath = "/categories/" + categorySlug;
        return this;
    }

    @Override
    protected String getPath() {
        return categoryPath != null ? categoryPath : super.getPath();
    }

    public CategoryPage openCategory(String categorySlug) {
        withCategory(categorySlug);
        open();
        return this;
    }
}