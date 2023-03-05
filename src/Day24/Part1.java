package Day24;

import java.util.*;

public class Part1 {
    public static void main(String[] args) {

        List<Group> immuneSystem = new ArrayList<>();

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

        List<Group> infection = new ArrayList<>();

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

        Map<Integer, Group> groupWithId = new HashMap<>();
        groupWithId.put(group1.getGroupNumber(), group1);
        groupWithId.put(group2.getGroupNumber(), group2);
        groupWithId.put(group3.getGroupNumber(), group3);
        groupWithId.put(group4.getGroupNumber(), group4);

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
