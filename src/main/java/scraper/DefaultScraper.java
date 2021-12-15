package scraper;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



public class DefaultScraper implements Scraper{
    private static final String PRICE_SELECTOR = ".detail__info-xlrg";
    private static final String BR_SELECTOR = ".nhs_BedsInfo";
    private static final String BA_SELECTOR = ".nhs_BathsInfo";
    private static final String GR_SELECTOR = ".nhs_GarageInfo";

    @Override @SneakyThrows
    public Home scrape(String url) {
        Document doc = Jsoup.connect(url).get();
        int price = getPrice(doc);
        double beds = getBeds(doc);
        double bathes = getBathes(doc);
        double garages = getGarages(doc);
        return new Home(price, beds, bathes, garages);
    }

    private int getPrice(Document doc) {
        String[] price = doc.selectFirst(PRICE_SELECTOR).text().replaceAll("[^0-9$]*", "").split("[$]");
        int priceLow = Integer.parseInt(price[1]);
        if (price.length > 2) {
            int priceHigh = Integer.parseInt(price[2]);
            return (priceHigh + priceLow) / 2;
        }
        return priceLow;
    }

    private double getBeds(Document doc) {
        String beds = doc.selectFirst(BR_SELECTOR).text().replaceAll("[^0-9.]*", "");
        return Double.parseDouble(beds);
    }

    private double getBathes(Document doc) {
        String bathes = doc.selectFirst(BA_SELECTOR).text().replaceAll("[^0-9.]*", "");
        return Double.parseDouble(bathes);
    }

    private double getGarages(Document doc) {
        String garages = doc.selectFirst(GR_SELECTOR).text().replaceAll("[^0-9.]*", "");
        return Double.parseDouble(garages);
    }
}
