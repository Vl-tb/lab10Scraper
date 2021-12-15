package scraper;

public class Main {
    public static void main(String[] args) {
        String url = "https://www.newhomesource.com/specdetail/130-victoria-peak-loop-dripping-springs-tx-78620/2108550";
        Scraper defaultScraper = new CacheScraper();
        Home home = defaultScraper.scrape(url);
        System.out.println(home);
    }
}
