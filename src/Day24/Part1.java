package Day24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Part1 {

    final static boolean TEST_MODE = false;

    public static void main(String[] args) throws IOException {

        List<Group> immuneSystem = new ArrayList<>();
        List<Group> infection = new ArrayList<>();

        Map<Integer, Group> groupWithId = new HashMap<>();
        if (TEST_MODE) {
            fillByTestData(immuneSystem, infection, groupWithId);
        } else {
            fillByRealData(immuneSystem, infection, groupWithId);
        }

        TreeSet<Group> immuneTargetSelectionQueue = new TreeSet<>(new TargetSelectionComparator());
        immuneTargetSelectionQueue.addAll(immuneSystem);

        TreeSet<Group> infectionTargetSelectionQueue = new TreeSet<>(new TargetSelectionComparator());
        infectionTargetSelectionQueue.addAll(infection);

        TreeSet<Group> attackQueue = new TreeSet<>();
        attackQueue.addAll(immuneSystem);
        attackQueue.addAll(infection);

        while (!immuneSystem.isEmpty() && !infection.isEmpty()) {
            targetSelection(infectionTargetSelectionQueue, immuneSystem);
            targetSelection(immuneTargetSelectionQueue, infection);

            for (Group groupWhichAttack : attackQueue) {
                if (groupWhichAttack.getGroupIdWhichIAttack() == -1) {
                    continue;
                }

                Group defenseGroup = groupWithId.get(groupWhichAttack.getGroupIdWhichIAttack());
                if (defenseGroup.getAttack(groupWhichAttack)) {
                    immuneSystem.remove(defenseGroup);
                    infection.remove(defenseGroup);
                    infectionTargetSelectionQueue.remove(defenseGroup);
                    immuneTargetSelectionQueue.remove(defenseGroup);
                    attackQueue.remove(defenseGroup);
                }
            }
        }

        List<Group> winner = immuneSystem.isEmpty() ? infection : immuneSystem;

        int counter = 0;
        for (Group group : winner) {
            counter += group.getUnits();
        }

        System.out.println(counter);

    }

    private static void fillByRealData(List<Group> immuneSystem, List<Group> infection, Map<Integer, Group> groupWithId) throws IOException {

        String path = ".\\src\\Day24\\data.txt";
        final String[] instructions = Files.readString(Path.of(path)).split("\r\n\r\n");

        for (String system : instructions) {
            String[] systemGroup = system.split("\r\n");
            for (int i = 0; i < systemGroup.length; i++) {
                if (i == 0) {
                    continue;
                }

                final String regex1 = "(\\d+) units each with (\\d+) hit points ";
                int units = Integer.parseInt(systemGroup[i].replaceAll(regex1, "$1"));
                int hitPoints = Integer.parseInt(systemGroup[i].replaceAll(regex1, "$2"));

                final String regex2 = " with an attack that does (\\d+) (\\w+) damage at initiative (\\d+)";
                int attackDamage = Integer.parseInt(systemGroup[i].replaceAll(regex2, "$1"));
                AttackType attackType = AttackType.valueOf(systemGroup[i].replaceAll(regex2, "$2").toUpperCase());
                int initiative = Integer.parseInt(systemGroup[i].replaceAll(regex2, "$3"));

                final String regex3 = "immune to ([a-z, ]+)";
                String[] immuneFeature = systemGroup[i].replaceAll(regex3, "$1").split(", ");
                Set<AttackType> immunities = new HashSet<>();
                for (String feature : immuneFeature) {
                    immunities.add(AttackType.valueOf(feature.toUpperCase()));
                }

                final String regex4 = "weak to ([a-z, ]+)";
                String[] weakFeature = systemGroup[i].replaceAll(regex4, "$1").split(", ");
                Set<AttackType> weaknesses = new HashSet<>();
                for (String feature : weakFeature) {
                    weaknesses.add(AttackType.valueOf(feature.toUpperCase()));
                }

                Group group = new Group(hitPoints, attackDamage, attackType, initiative, immunities, weaknesses, units);

                if (systemGroup[0].equals("Immune System:")) {
                    immuneSystem.add(group);
                } else {
                    infection.add(group);
                }

                groupWithId.put(group.getGroupNumber(), group);
            }
        }


//        final String way = Files.readString(Path.of(path));

//        String path = ".\\src\\Day6\\data.txt";
//        final String[] instructions = Files.readString(Path.of(path)).lines().toArray(String[]::new);
//
//        for (String str : instructions) {
//            final String regex = "^(toggle|turn off|turn on) (\\d+,\\d+) (through) (\\d+,\\d+)$";
//
//            String todo = str.replaceAll(regex, "$1");
//            String[] coord1 = str.replaceAll(regex, "$2").split(",");
//            String[] coord2 = str.replaceAll(regex, "$4").split(",");
//
//            setUpLights(todo, coord1, coord2);
//        }

    }

    private static void fillByTestData(List<Group> immuneSystem, List<Group> infection, Map<Integer, Group> groupWithId) {

        Set<AttackType> weaknesses = new HashSet<>();
        weaknesses.add(AttackType.RADIATION);
        weaknesses.add(AttackType.BLUDGEONING);

        Group group1 = new Group(5390, 4507, AttackType.FIRE, 2, new HashSet<>(), weaknesses,17);

        weaknesses = new HashSet<>();
        weaknesses.add(AttackType.BLUDGEONING);
        weaknesses.add(AttackType.SLASHING);

        Set<AttackType> immunities = new HashSet<>();
        immunities.add(AttackType.FIRE);

        Group group2 = new Group(1274, 25, AttackType.SLASHING, 3, immunities, weaknesses, 989);

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

        groupWithId.put(group1.getGroupNumber(), group1);
        groupWithId.put(group2.getGroupNumber(), group2);
        groupWithId.put(group3.getGroupNumber(), group3);
        groupWithId.put(group4.getGroupNumber(), group4);
    }

    private static void targetSelection(TreeSet<Group> whoAttack, List<Group> whoDefends){
        Set<Integer> chosenGroups = new HashSet<>();

        for (Group groupWhichChoose : whoAttack) {

            groupWhichChoose.setGroupIdWhichIAttack(-1);

            int maxDamage = Integer.MIN_VALUE;
            int maxDamageId = -1;

            for (Group groupWhichDefend: whoDefends) {
                if (chosenGroups.contains(groupWhichDefend.getGroupNumber())) {
                    continue;
                }
                int damage = groupWhichDefend.calculateDamage(groupWhichChoose);
                if (damage > maxDamage) {
                    maxDamageId = groupWhichDefend.getGroupNumber();
                    maxDamage = damage;
                }
            }

            if (maxDamage == 0) {
                continue;
            }

            int counter = 0;
            for (Group groupWhichDefend: whoDefends) {
                if (chosenGroups.contains(groupWhichDefend.getGroupNumber())) {
                    continue;
                }

                if (maxDamage == groupWhichDefend.calculateDamage(groupWhichChoose)) {
                    counter++;
                }
            }

            if (counter == 1) {
                groupWhichChoose.setGroupIdWhichIAttack(maxDamageId);
                chosenGroups.add(maxDamageId);
                continue;
            }

//________________________________________________________________________________________________________

            int maxEffectivePower = Integer.MIN_VALUE;
            int effectivePowerId = -1;

            for (Group groupWhichDefend: whoDefends) {
                if (chosenGroups.contains(groupWhichDefend.getGroupNumber())) {
                    continue;
                }
                int effectivePower = groupWhichDefend.getEffectivePower();
                if (effectivePower > maxEffectivePower) {
                    effectivePowerId = groupWhichDefend.getGroupNumber();
                    maxEffectivePower = effectivePower;
                }
            }

            counter = 0;
            for (Group groupWhichDefend: whoDefends) {
                if (chosenGroups.contains(groupWhichDefend.getGroupNumber())) {
                    continue;
                }

                if (maxDamage == groupWhichDefend.calculateDamage(groupWhichChoose) && maxEffectivePower == groupWhichDefend.getEffectivePower()) {
                    counter++;
                }
            }

            if (counter == 1) {
                groupWhichChoose.setGroupIdWhichIAttack(effectivePowerId);
                chosenGroups.add(effectivePowerId);
                continue;
            }

//___________________________________________________________________________________________________________

            int maxInitiative = Integer.MIN_VALUE;
            int initiativeId = -1;

            for (Group groupWhichDefend: whoDefends) {
                if (chosenGroups.contains(groupWhichDefend.getGroupNumber())) {
                    continue;
                }
                int initiative = groupWhichDefend.getInitiative();
                if (initiative > maxInitiative) {
                    initiativeId = groupWhichDefend.getGroupNumber();
                    maxInitiative = initiative;
                }
            }

            groupWhichChoose.setGroupIdWhichIAttack(initiativeId);
            chosenGroups.add(initiativeId);
        }

    }
}

class TargetSelectionComparator implements Comparator<Group> {
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
}
