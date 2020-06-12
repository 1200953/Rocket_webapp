package rockets.mining;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.dataaccess.neo4j.Neo4jDAO;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
import rockets.model.Rocket;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static rockets.model.Launch.LaunchOutcome.FAILED;
import static rockets.model.Launch.LaunchOutcome.SUCCESSFUL;

public class RocketMinerUnitTest {
    Logger logger = LoggerFactory.getLogger(RocketMinerUnitTest.class);

    private DAO dao;
    private RocketMiner miner;
    private List<Rocket> rockets;
    private List<LaunchServiceProvider> lsps;
    private List<Launch> launches;
//start test
    @BeforeEach
    public void setUp() {
        dao = mock(Neo4jDAO.class);
        miner = new RocketMiner(dao);
        rockets = Lists.newArrayList();

        lsps = Arrays.asList(
                new LaunchServiceProvider("ULA", 1990, "USA"),
                new LaunchServiceProvider("SpaceX", 2002, "USA"),
                new LaunchServiceProvider("ESA", 1975, "Europe ")
        );

        // index of lsp of each rocket
        int[] lspIndex = new int[]{0, 0, 0, 1, 1};
        // index of lsp of each launch
        int[] lspIndexForLaunch = new int[]{0, 1, 0, 1, 0, 2, 0, 0, 0, 0};
        // 5 rockets
        for (int i = 0; i < 5; i++) {
            rockets.add(new Rocket("rocket_" + i, "USA", lsps.get(lspIndex[i])));
        }
        // month of each launch
        int[] months = new int[]{1, 6, 4, 3, 4, 11, 6, 5, 12, 5};

        // index of rocket of each launch
        int[] rocketIndex = new int[]{0, 0, 0, 0, 1, 1, 1, 2, 2, 3};

        // add launch outcome
        Launch.LaunchOutcome[] launchOutcomes = new Launch.LaunchOutcome[]{FAILED, SUCCESSFUL, FAILED,
                SUCCESSFUL, SUCCESSFUL, SUCCESSFUL, FAILED, FAILED, FAILED, SUCCESSFUL};

        //int[] priceOfLaunch = new int[]{1, 0, 0, 0, 1, 2, 1, 2, 4, 5};

        // add price
        BigDecimal[] price = new BigDecimal[]{BigDecimal.valueOf(10000), BigDecimal.valueOf(20000),
                BigDecimal.valueOf(30000), BigDecimal.valueOf(40000), BigDecimal.valueOf(50000),
                BigDecimal.valueOf(60000), BigDecimal.valueOf(70000), BigDecimal.valueOf(80000),
                BigDecimal.valueOf(90000), BigDecimal.valueOf(100000)};

        String arr[] = {"100000", "200000", "300000", "400000", "500000", "600000", "700000", "800000", "900000",
                "1000000"};
        Set<String> payload = new HashSet<>(Arrays.asList(arr));


        // int[] launchOutcomeIndex = new int[]{0,1,2,3,4,5,6,7,8,9};
        // 10 launches
        launches = IntStream.range(0, 10).mapToObj(i -> {
            logger.info("create " + i + " launch in month: " + months[i]);
            Launch l = new Launch();
            l.setLaunchDate(LocalDate.of(2017, months[i], 1));
            l.setLaunchVehicle(rockets.get(rocketIndex[i]));
            l.setLaunchSite("VAFB");
            l.setOrbit("LEO");
            l.setLaunchServiceProvider(lsps.get(lspIndexForLaunch[i]));
            // l.setLaunchOutcome(launchOutcomes[launchOutcomeIndex[i]]);
            l.setLaunchOutcome(launchOutcomes[i]);
            l.setPrice(price[i]);
            l.setPayload(payload);
            spy(l);
            return l;
        }).collect(Collectors.toList());
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostLaunchedRockets(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);

        Collection<Launch> newLaunches = launches;
        Collection<Rocket> rockets = new ArrayList<>();
        for (Launch l : newLaunches) {
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

        List<Rocket> loadedRockets = miner.mostLaunchedRockets(k);
        assertEquals(k, loadedRockets.size());
        assertEquals(roc.subList(0, k), loadedRockets);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostReliableLaunchServiceProviders(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        Collection<Launch> newLaunches = launches;
        Collection<LaunchServiceProvider> success = new ArrayList<>();
        Collection<LaunchServiceProvider> failed = new ArrayList<>();
        for (Launch l : newLaunches) {
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
        Comparator<Map.Entry<LaunchServiceProvider, Integer>> compare =
                Comparator.comparing(Map.Entry<LaunchServiceProvider, Integer>::getValue);
        Collections.sort(list, compare.reversed());

        int count = 0;
        List<LaunchServiceProvider> roc = new ArrayList<>();
        for (Map.Entry<LaunchServiceProvider, Integer> entry : list) {
            roc.add(entry.getKey());
            ++count;
            if (count >= k) {
                break;
            }
        }

        List<LaunchServiceProvider> loadedProviders = miner.mostReliableLaunchServiceProviders(k);
        assertEquals(k, loadedProviders.size());
        assertEquals(roc.subList(0, k), loadedProviders);

    }

    @ParameterizedTest
    @ValueSource(strings = {"LEO"})
    public void shouldReturnTopDominantCountries(String orbit) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        Collection<Launch> newLaunches = launches;
        Collection<Rocket> roc = new ArrayList<>();
        for (Launch l : newLaunches) {
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

        String loadedCountries = miner.dominantCountry(orbit);
        assertEquals(coun, loadedCountries);

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnHighestRevenueLaunchServiceProvidersWithFixedYear(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);

        int year = 2017;
        Collection<Launch> newLaunches = launches;
        Map<LaunchServiceProvider, BigDecimal> LSPMap = new HashMap<>();
        for (Launch l : newLaunches) {
            if (l.getLaunchDate().getYear() == year)
                LSPMap.put(l.getLaunchServiceProvider(), l.getPrice());
        }
        Map<LaunchServiceProvider, BigDecimal> priceMap = new HashMap<>();
        for (Map.Entry<LaunchServiceProvider, BigDecimal> entry : LSPMap.entrySet()) {
            BigDecimal count = priceMap.get(entry.getKey());
            priceMap.put(entry.getKey(), (count == null) ? entry.getValue() : count.add(entry.getValue()));
        }
        List<Map.Entry<LaunchServiceProvider, BigDecimal>> list = new ArrayList<>(priceMap.entrySet());
        Comparator<Map.Entry<LaunchServiceProvider, BigDecimal>> compare =
                Comparator.comparing(Map.Entry<LaunchServiceProvider, BigDecimal>::getValue);
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
        List<LaunchServiceProvider> loadedProvider = miner.highestRevenueLaunchServiceProviders(k, year);
        assertEquals(k, loadedProvider.size());
        assertEquals(launchServiceProviders.subList(0, k), loadedProvider);
    }

    @ParameterizedTest
    @ValueSource(ints = {2017})
    public void shouldReturnHighestRevenueLaunchServiceProvidersWithFixedK(int year) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);

        int k = 3;
        Collection<Launch> newLaunches = launches;
        Map<LaunchServiceProvider, BigDecimal> LSPMap = new HashMap<>();
        for (Launch l : newLaunches) {
            if (l.getLaunchDate().getYear() == year)
                LSPMap.put(l.getLaunchServiceProvider(), l.getPrice());
        }
        Map<LaunchServiceProvider, BigDecimal> priceMap = new HashMap<>();
        for (Map.Entry<LaunchServiceProvider, BigDecimal> entry : LSPMap.entrySet()) {
            BigDecimal count = priceMap.get(entry.getKey());
            priceMap.put(entry.getKey(), (count == null) ? entry.getValue() : count.add(entry.getValue()));
        }
        List<Map.Entry<LaunchServiceProvider, BigDecimal>> list = new ArrayList<>(priceMap.entrySet());
        Comparator<Map.Entry<LaunchServiceProvider, BigDecimal>> compare =
                Comparator.comparing(Map.Entry<LaunchServiceProvider, BigDecimal>::getValue);
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
        List<LaunchServiceProvider> loadedProvider = miner.highestRevenueLaunchServiceProviders(k, year);
        assertEquals(k, loadedProvider.size());
        assertEquals(launchServiceProviders.subList(0, k), loadedProvider);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostRecentLaunches(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> sortedLaunches = new ArrayList<>(launches);
        sortedLaunches.sort((a, b) -> -a.getLaunchDate().compareTo(b.getLaunchDate()));
        List<Launch> loadedLaunches = miner.mostRecentLaunches(k);
        assertEquals(k, loadedLaunches.size());
        assertEquals(sortedLaunches.subList(0, k), loadedLaunches);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostExpensiveLaunches(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> sortedLaunches = new ArrayList<>(launches);
        sortedLaunches.sort((a, b) -> -a.getPrice().compareTo(b.getPrice()));
        List<Launch> loadedLaunches = miner.mostExpensiveLaunches(k);
        assertEquals(k, loadedLaunches.size());
        assertEquals(sortedLaunches.subList(0, k), loadedLaunches);
    }
}