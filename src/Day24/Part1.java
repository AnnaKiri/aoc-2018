package Day24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Part1 {

    final static boolean TEST_MODE = false;

    public static void main(String[] args) throws IOException {

        for (int boost = 0; boost < 1_000_000_000; boost++) {
            if (simulationStart(boost)) {
                break;
            }
        }
    }

    private static boolean simulationStart(int boost) throws IOException {

        List<Group> immuneSystem = new ArrayList<>();
        List<Group> infection = new ArrayList<>();

        Map<Integer, Group> groupsById = new HashMap<>();
        if (TEST_MODE) {
            fillByTestData(immuneSystem, infection, groupsById, boost);
        } else {
            fillByRealData(immuneSystem, infection, groupsById, boost);
        }

        int battle = 0;
        while (!immuneSystem.isEmpty() && !infection.isEmpty()) {
            battle++;
            if (battle > 10000) {
                return false;
            }

            TreeSet<Group> aggressorsQueue = new TreeSet<>();
            aggressorsQueue.addAll(immuneSystem);
            aggressorsQueue.addAll(infection);

            targetSelection(infection, immuneSystem);
            targetSelection(immuneSystem, infection);

            for (Group aggressor : aggressorsQueue) {
                if (aggressor.getMyVictimId() == -1 || aggressor.getUnits() == 0) {
                    continue;
                }

                Group victim = groupsById.get(aggressor.getMyVictimId());
                if (victim.getAttack()) {
                    immuneSystem.remove(victim);
                    infection.remove(victim);
                }
            }

            Iterator<Group> iterator = aggressorsQueue.iterator();
            while (iterator.hasNext()) {
                Group group = iterator.next();
                if (group.getUnits() == 0) {
                    iterator.remove();
                }
            }
        }

        List<Group> winner = immuneSystem.isEmpty() ? infection : immuneSystem;

        int counter = 0;
        for (Group group : winner) {
            counter += group.getUnits();
        }

        System.out.println(counter);

        return infection.isEmpty();
    }

    private static void targetSelection(List<Group> aggressors, List<Group> victims) {
        TreeSet<Group> aggressorsQueue = new TreeSet<>(new Comparator<Group>() {
            @Override
            public int compare(Group o1, Group o2) {
                if (o1.getEffectivePower() < o2.getEffectivePower()){
                    return 1;
                } else if (o1.getEffectivePower() > o2.getEffectivePower()) {
                    return -1;
                } else {
                    return o2.getInitiative() - o1.getInitiative();
                }
            }
        });
        aggressorsQueue.addAll(aggressors);

        Set<Integer> chosenVictims = new HashSet<>();

        for (Group aggressor : aggressorsQueue) {
            aggressor.setMyVictimId(-1);

            TreeSet<Group> candidatesToAttack = new TreeSet<>(new Comparator<Group>() {
                @Override
                public int compare(Group o1, Group o2) {
                    if (o1.calculateDamage() < o2.calculateDamage()){
                        return 1;
                    } else if (o1.calculateDamage() > o2.calculateDamage()) {
                        return -1;
                    } else {
                        if (o1.getEffectivePower() < o2.getEffectivePower()) {
                            return 1;
                        } else if (o1.getEffectivePower() > o2.getEffectivePower()) {
                            return -1;
                        } else {
                            return o2.getInitiative() - o1.getInitiative();
                        }
                    }
                }
            });

            for (Group victim: victims) {
                if (chosenVictims.contains(victim.getId())) {
                    continue;
                }
                victim.setMyAggressor(aggressor);
                candidatesToAttack.add(victim);
            }

            if (candidatesToAttack.isEmpty()) {
                continue;
            }

            Group bestChoice = candidatesToAttack.first();
            if (bestChoice.calculateDamage() != 0) {
                aggressor.setMyVictimId(bestChoice.getId());
                chosenVictims.add(bestChoice.getId());
            }

            for (Group victim: victims) {
                if (chosenVictims.contains(victim.getId())) {
                    continue;
                }
                victim.setMyAggressor(null);
            }
        }

    }

    private static void fillByRealData(List<Group> immuneSystem, List<Group> infection, Map<Integer, Group> groupById, int boost) throws IOException {

        String path = ".\\src\\Day24\\data.txt";
        final String[] armiesDescription = Files.readString(Path.of(path)).split("\r\n\r\n");

        for (String system : armiesDescription) {
            String[] systemGroup = system.split("\r\n");
            for (int i = 0; i < systemGroup.length; i++) {
                if (i == 0) {
                    continue;
                }

                Matcher regex1 = Pattern.compile("(\\d+) units each with (\\d+) hit points ").matcher(systemGroup[i]);

                if (!regex1.find()) {
                    System.out.println("regex1 not found!");
                    return;
                }
                int units = Integer.parseInt(regex1.group(1));
                int hitPoints = Integer.parseInt(regex1.group(2));

                Matcher regex2 = Pattern.compile(" with an attack that does (\\d+) (\\w+) damage at initiative (\\d+)").matcher(systemGroup[i]);

                if (!regex2.find()) {
                    System.out.println("regex2 not found!");
                    return;
                }
                int attackDamage = Integer.parseInt(regex2.group(1));
                if (systemGroup[0].equals("Immune System:")) {
                    attackDamage += boost;
                }
                AttackType attackType = AttackType.valueOf(regex2.group(2).toUpperCase());
                int initiative = Integer.parseInt(regex2.group(3));

                Matcher regex3 = Pattern.compile("immune to ([a-z, ]+)").matcher(systemGroup[i]);
                Set<AttackType> immunities = new HashSet<>();
                parseListOfAttack(regex3, immunities);

                Matcher regex4 = Pattern.compile("weak to ([a-z, ]+)").matcher(systemGroup[i]);
                Set<AttackType> weaknesses = new HashSet<>();
                parseListOfAttack(regex4, weaknesses);

                Group group = new Group(hitPoints, attackDamage, attackType, initiative, immunities, weaknesses, units);

                if (systemGroup[0].equals("Immune System:")) {
                    immuneSystem.add(group);
                } else {
                    infection.add(group);
                }

                groupById.put(group.getId(), group);
            }
        }
    }

    private static void parseListOfAttack(Matcher regex, Set<AttackType> list) {
        if (regex.find()) {
            String[] attackType = regex.group(1).split(", ");
            for (String feature : attackType) {
                list.add(AttackType.valueOf(feature.toUpperCase()));
            }
        }
    }

    private static void fillByTestData(List<Group> immuneSystem, List<Group> infection, Map<Integer, Group> groupById, int boost) {

        Set<AttackType> weaknesses = new HashSet<>();
        weaknesses.add(AttackType.RADIATION);
        weaknesses.add(AttackType.BLUDGEONING);

        Group group1 = new Group(5390, 4507 + boost, AttackType.FIRE, 2, new HashSet<>(), weaknesses,17);

        weaknesses = new HashSet<>();
        weaknesses.add(AttackType.BLUDGEONING);
        weaknesses.add(AttackType.SLASHING);

        Set<AttackType> immunities = new HashSet<>();
        immunities.add(AttackType.FIRE);

        Group group2 = new Group(1274, 25 + boost, AttackType.SLASHING, 3, immunities, weaknesses, 989);

        immuneSystem.add(group1);
        immuneSystem.add(group2);

        weaknesses = new HashSet<>();
        weaknesses.add(AttackType.RADIATION);

        Group group3 = new Group(4706, 116, AttackType.BLUDGEONING, 1, new HashSet<>(), weaknesses, 801);

        immunities = new HashSet<>();
        immunities.add(AttackType.RADIATION);

        weaknesses = new HashSet<>();
        weaknesses.add(AttackType.FIRE);
        weaknesses.add(AttackType.COLD);

        Group group4 = new Group(2961, 12, AttackType.SLASHING, 4, immunities, weaknesses, 4485);

        infection.add(group3);
        infection.add(group4);

        groupById.put(group1.getId(), group1);
        groupById.put(group2.getId(), group2);
        groupById.put(group3.getId(), group3);
        groupById.put(group4.getId(), group4);
    }
}
