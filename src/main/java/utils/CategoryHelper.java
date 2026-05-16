package utils;

import java.util.HashMap;
import java.util.Map;

public final class CategoryHelper {

    private static final Map<String, String> CATEGORY_SLUG_MAP = new HashMap<>();

    static {
        CATEGORY_SLUG_MAP.put("программирование", "programming");
        CATEGORY_SLUG_MAP.put("архитектура", "architecture");
        CATEGORY_SLUG_MAP.put("data science", "data-science");
        CATEGORY_SLUG_MAP.put("инфраструктура", "operations");
        CATEGORY_SLUG_MAP.put("gamedev", "gamedev");
        CATEGORY_SLUG_MAP.put("безопасность", "information-security-courses");
        CATEGORY_SLUG_MAP.put("управление", "marketing-business");
        CATEGORY_SLUG_MAP.put("аналитика и анализ", "analytics");
        CATEGORY_SLUG_MAP.put("бизнес и продукт в it", "business-product");
        CATEGORY_SLUG_MAP.put("импортозамещение", "import-substitution");
        CATEGORY_SLUG_MAP.put("тестирование", "testing");
        CATEGORY_SLUG_MAP.put("нейросети", "neural_networks");
        CATEGORY_SLUG_MAP.put("it без программирования", "it-bez-programmirovanija");
        CATEGORY_SLUG_MAP.put("корпоративные курсы", "corporate");
    }

    private CategoryHelper() {}

    public static String getSlug(String categoryName) {
        String key = categoryName.toLowerCase();
        return CATEGORY_SLUG_MAP.getOrDefault(key, key.replace(" ", "-"));
    }
}