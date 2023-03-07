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

    private static boolean simulationStart( int boost) throws IOException {

        List<Group> immuneSystem = new ArrayList<>();
        List<Group> infection = new ArrayList<>();

        Map<Integer, Group> groupWithId = new HashMap<>();
        if (TEST_MODE) {
            fillByTestData(immuneSystem, infection, groupWithId, boost);
        } else {
            fillByRealData(immuneSystem, infection, groupWithId, boost);
        }

        int battle = 0;
        while (!immuneSystem.isEmpty() && !infection.isEmpty()) {
            battle++;
            if (battle > 10000) {
                return false;
            }

            TreeSet<Group> attackQueue = new TreeSet<>();
            attackQueue.addAll(immuneSystem);
            attackQueue.addAll(infection);

            targetSelection(infection, immuneSystem);
            targetSelection(immuneSystem, infection);

            for (Group groupWhichAttack : attackQueue) {
                groupWhichAttack.updateEffectivePower();

                if (groupWhichAttack.getGroupIdWhichIAttack() == -1 || groupWhichAttack.getUnits() == 0) {
                    continue;
                }

                Group defenseGroup = groupWithId.get(groupWhichAttack.getGroupIdWhichIAttack());
                if (defenseGroup.performAttack()) {
                    immuneSystem.remove(defenseGroup);
                    infection.remove(defenseGroup);
                }
            }

            Iterator<Group> iterator = attackQueue.iterator();
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

    private static void targetSelection(List<Group> whoAttack, List<Group> whoDefends){
        TreeSet<Group> selectionQueue = new TreeSet<>(new Comparator<Group>() {
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
        selectionQueue.addAll(whoAttack);

        Set<Integer> chosenGroups = new HashSet<>();

        for (Group groupWhichChoose : selectionQueue) {
            groupWhichChoose.setGroupIdWhichIAttack(-1);

            int maxDamage = Integer.MIN_VALUE;
            int maxDamageId = -1;

            for (Group groupWhichDefend: whoDefends) {
                if (chosenGroups.contains(groupWhichDefend.getGroupNumber())) {
                    continue;
                }

                groupWhichDefend.setWhoAttackMe(groupWhichChoose);
                int damage = groupWhichDefend.calculateDamage();
                if (damage > maxDamage) {
                    maxDamageId = groupWhichDefend.getGroupNumber();
                    maxDamage = damage;
                }
            }

            if (maxDamage == 0 || maxDamageId == -1) {
                continue;
            }

            int counter = 0;
            for (Group groupWhichDefend: whoDefends) {
                if (chosenGroups.contains(groupWhichDefend.getGroupNumber())) {
                    continue;
                }

                if (maxDamage == groupWhichDefend.calculateDamage()) {
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

                if (maxDamage == groupWhichDefend.calculateDamage()) {
                    int effectivePower = groupWhichDefend.getEffectivePower();
                    if (effectivePower > maxEffectivePower) {
                        effectivePowerId = groupWhichDefend.getGroupNumber();
                        maxEffectivePower = effectivePower;
                    }
                }
            }

            counter = 0;
            for (Group groupWhichDefend: whoDefends) {
                if (chosenGroups.contains(groupWhichDefend.getGroupNumber())) {
                    continue;
                }

                if (maxDamage == groupWhichDefend.calculateDamage() && maxEffectivePower == groupWhichDefend.getEffectivePower()) {
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

                if (maxDamage == groupWhichDefend.calculateDamage() && maxEffectivePower == groupWhichDefend.getEffectivePower()) {
                    int initiative = groupWhichDefend.getInitiative();
                    if (initiative > maxInitiative) {
                        initiativeId = groupWhichDefend.getGroupNumber();
                        maxInitiative = initiative;
                    }
                }
            }

            groupWhichChoose.setGroupIdWhichIAttack(initiativeId);
            chosenGroups.add(initiativeId);

            for (Group groupWhichDefend: whoDefends) {
                if (chosenGroups.contains(groupWhichDefend.getGroupNumber())) {
                    continue;
                }
                groupWhichDefend.setWhoAttackMe(null);
            }
        }
    }

    private static void fillByRealData(List<Group> immuneSystem, List<Group> infection, Map<Integer, Group> groupWithId, int boost) throws IOException {

        String path = ".\\src\\Day24\\data.txt";
        final String[] instructions = Files.readString(Path.of(path)).split("\r\n\r\n");

        for (String system : instructions) {
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
                if (regex3.find()) {
                    String[] immuneFeature = regex3.group(1).split(", ");
                    for (String feature : immuneFeature) {
                        immunities.add(AttackType.valueOf(feature.toUpperCase()));
                    }
                }

                Matcher regex4 = Pattern.compile("weak to ([a-z, ]+)").matcher(systemGroup[i]);
                Set<AttackType> weaknesses = new HashSet<>();
                if (regex4.find()) {
                    String[] weakFeature = regex4.group(1).split(", ");
                    for (String feature : weakFeature) {
                        weaknesses.add(AttackType.valueOf(feature.toUpperCase()));
                    }
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
    }

    private static void fillByTestData(List<Group> immuneSystem, List<Group> infection, Map<Integer, Group> groupWithId, int boost) {

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

        groupWithId.put(group1.getGroupNumber(), group1);
        groupWithId.put(group2.getGroupNumber(), group2);
        groupWithId.put(group3.getGroupNumber(), group3);
        groupWithId.put(group4.getGroupNumber(), group4);
    }
}

class TargetSelectionComparator implements Comparator<Group> {
    @Override
    public int compare(Group o1, Group o2) {
        if (o1.getAttackDamage() < o2.getAttackDamage()){
            return 1;
        } else if (o1.getAttackDamage() > o2.getAttackDamage()) {
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
}
