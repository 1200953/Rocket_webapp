package rockets.mining;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
import rockets.model.Rocket;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class RocketMiner {
    private static Logger logger = LoggerFactory.getLogger(RocketMiner.class);

    private DAO dao;


    public RocketMiner(DAO dao) {
        this.dao = dao;
    }

    /**
     * TODO: to be implemented & tested!
     * Returns the top-k most active rockets, as measured by number of completed launches.
     *
     * @param k the number of rockets to be returned.
     * @return the list of k most active rockets.
     */
    public List<Rocket> mostLaunchedRockets(int k) {

        logger.info("find most active " + k + " rockets");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Collection<Rocket> rockets = new ArrayList<>();
        for (Launch l : launches) {
            rockets.add(l.getLaunchVehicle());
        }


        Map<Rocket, Integer> freqMap = new HashMap<>();
        for (Rocket r : rockets) {
            Integer count = freqMap.get(r);
            freqMap.put(r, (count == null) ? 1 : count + 1);
        }

        List<Map.Entry<Rocket, Integer>> list = new ArrayList<>(freqMap.entrySet());
        Comparator<Map.Entry<Rocket, Integer>> compare = Comparator.comparing(Map.Entry<Rocket, Integer>::getValue);
        Collections.sort(list, compare.reversed());

        int count = 0;
        List<Rocket> roc = new ArrayList<>();
        for (Map.Entry<Rocket, Integer> entry : list) {
            roc.add(entry.getKey());
            ++count;
            if (count >= k) {
                break;
            }
        }
        return roc;

    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most reliable launch service providers as measured
     * by percentage of successful launches.
     *
     * @param k the number of launch service providers to be returned.
     * @return the list of k most reliable ones.
     */
    public List<LaunchServiceProvider> mostReliableLaunchServiceProviders(int k) {
        logger.info("find most reliable " + k + " launch service providers");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Collection<LaunchServiceProvider> success = new ArrayList<>();
        Collection<LaunchServiceProvider> failed = new ArrayList<>();
        for (Launch l : launches) {
            if (l.getLaunchOutcome().name().equals("SUCCESSFUL")) {
                success.add(l.getLaunchServiceProvider());
            } else {
                failed.add(l.getLaunchServiceProvider());
            }
        }
        Map<LaunchServiceProvider, Integer> successMap = new HashMap<>();
        for (LaunchServiceProvider l : success) {
            Integer count = successMap.get(l);
            successMap.put(l, (count == null) ? 1 : count + 1);
        }
        Map<LaunchServiceProvider, Integer> failMap = new HashMap<>();
        for (LaunchServiceProvider l : failed) {
            Integer count = failMap.get(l);
            failMap.put(l, (count == null) ? 1 : count + 1);
        }
        Map<LaunchServiceProvider, Integer> finishMap = new HashMap<>();
        for (Map.Entry<LaunchServiceProvider, Integer> entry : successMap.entrySet()) {
            for (Map.Entry<LaunchServiceProvider, Integer> ent : failMap.entrySet()) {
                if (entry.getKey().equals(ent.getKey())) {
                    finishMap.put(entry.getKey(), entry.getValue() / (entry.getValue() + ent.getValue()));
                } else {
                    finishMap.put(entry.getKey(), 1);
                }
            }
        }

        List<Map.Entry<LaunchServiceProvider, Integer>> list = new ArrayList<>(finishMap.entrySet());
        Comparator<Map.Entry<LaunchServiceProvider, Integer>> compare = Comparator.comparing(Map.Entry<LaunchServiceProvider, Integer>::getValue);
        Collections.sort(list, compare.reversed());


        // 取出前k个元素
        int count = 0;
        List<LaunchServiceProvider> roc = new ArrayList<>();
        for (Map.Entry<LaunchServiceProvider, Integer> entry : list) {
            roc.add(entry.getKey());
            ++count;
            if (count >= k) {
                break;
            }
        }
        return roc;


    }

    /**
     * <p>
     * Returns the top-k most recent launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most recent launches.
     */
    public List<Launch> mostRecentLaunches(int k) {
        logger.info("find most recent " + k + " launches");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Comparator<Launch> launchDateComparator = (a, b) -> -a.getLaunchDate().compareTo(b.getLaunchDate());
        return launches.stream().sorted(launchDateComparator).limit(k).collect(Collectors.toList());
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the dominant country who has the most launched rockets in an orbit.
     *
     * @param orbit the orbit
     * @return the country who sends the most payload to the orbit
     */
    public String dominantCountry(String orbit) {
        logger.info("find most dominant country in an orbit");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Collection<Rocket> roc = new ArrayList<>();
        for (Launch l : launches) {
            if (l.getLaunchOutcome().name().equals("SUCCESSFUL") && l.getOrbit().equals(orbit)) {
                roc.add(l.getLaunchVehicle());
            }

        }
        ArrayList<String> country = new ArrayList<>();
        for (Rocket r : roc) {
            country.add(r.getCountry());
        }
        Map<String, Integer> countryMap = new HashMap<>();
        for (String c : country) {
            Integer count = countryMap.get(c);
            countryMap.put(c, (count == null) ? 1 : count + 1);
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(countryMap.entrySet());
        Comparator<Map.Entry<String, Integer>> compare = Comparator.comparing(Map.Entry<String, Integer>::getValue);
        Collections.sort(list, compare.reversed());
        String coun = list.get(0).getKey();
        return coun;

    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most expensive launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most expensive launches.
     */
    public List<Launch> mostExpensiveLaunches(int k) {
        logger.info("find most expensive" + k + "launches");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Map<Launch, BigDecimal> launchMap = new HashMap<>();
        for (Launch l : launches) {
            launchMap.put(l, l.getPrice());
        }
        List<Map.Entry<Launch, BigDecimal>> list = new ArrayList<>(launchMap.entrySet());
        Comparator<Map.Entry<Launch, BigDecimal>> compare = Comparator.comparing(Map.Entry<Launch, BigDecimal>::getValue);
        Collections.sort(list, compare.reversed());
        int count = 0;
        List<Launch> launch = new ArrayList<>();
        for (Map.Entry<Launch, BigDecimal> entry : list) {
            launch.add(entry.getKey());
            ++count;
            if (count >= k) {
                break;
            }
        }
        return launch;

    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns a list of launch service provider that has the top-k highest
     * sales revenue in a year.
     *
     * @param k    the number of launch service provider.
     * @param year the year in request
     * @return the list of k launch service providers who has the highest sales revenue.
     */
    public List<LaunchServiceProvider> highestRevenueLaunchServiceProviders(int k, int year) {
        logger.info("find highest sales revenue" + k + "launch service provider in" + year);
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Map<LaunchServiceProvider, BigDecimal> LSPMap = new HashMap<>();
        for (Launch l : launches) {
            if (l.getLaunchDate().getYear() == year)
                LSPMap.put(l.getLaunchServiceProvider(), l.getPrice());
        }
        Map<LaunchServiceProvider, BigDecimal> priceMap = new HashMap<>();
        for (Map.Entry<LaunchServiceProvider, BigDecimal> entry : LSPMap.entrySet()) {
            BigDecimal count = priceMap.get(entry.getKey());
            priceMap.put(entry.getKey(), (count == null) ? entry.getValue() : count.add(entry.getValue()));
        }
        List<Map.Entry<LaunchServiceProvider, BigDecimal>> list = new ArrayList<>(priceMap.entrySet());
        Comparator<Map.Entry<LaunchServiceProvider, BigDecimal>> compare = Comparator.comparing(Map.Entry<LaunchServiceProvider, BigDecimal>::getValue);
        Collections.sort(list, compare.reversed());
        int count = 0;
        List<LaunchServiceProvider> launchServiceProviders = new ArrayList<>();
        for (Map.Entry<LaunchServiceProvider, BigDecimal> entry : list) {
            launchServiceProviders.add(entry.getKey());
            ++count;
            if (count >= k) {
                break;
            }
        }
        return launchServiceProviders;
    }
}
