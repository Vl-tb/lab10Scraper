package scraper;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CacheScraper implements Scraper {
    private Scraper scraper = new DefaultScraper();

    @Override @SneakyThrows
    public Home scrape(String url) {
        // Created connection to DB
        Connection connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
        Statement statement = connection.createStatement();

        // Execute query
        String query = String.format("select count(*) as count from homes where url='%s'", url);
        ResultSet rs = statement.executeQuery(query);

        // Extract result
        if (rs.getInt("count") > 0) {
            // Extract from DB
            query = String.format("select * from homes where url='%s'", url);
            rs = statement.executeQuery(query);
            System.out.println("yes");
            return new Home(rs.getInt("price"), rs.getDouble("beds"), rs.getDouble("bathes"), rs.getDouble("garages"));
        } else {
            // Call old scraper
            Home home = scraper.scrape(url);
            int price = home.getPrice();
            double beds = home.getBeds();
            double bathes = home.getBathes();
            double garages = home.getGarages();
            query = String.format("insert into homes(url, price, beds, bathes, garages) values ('%s', '%d', '%f', '%f', '%f')", url, price, beds, bathes, garages);
            statement.executeUpdate(query);
            return home;
        }
    }
}
