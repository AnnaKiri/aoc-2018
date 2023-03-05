package Day24;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

public class Group implements Comparable<Group> {

    private static int groupCounter = 0;

    private int hitPoints;
    private int attackDamage;
    private AttackType attackType;
    private int initiative;
    private Set<AttackType> weaknesses;
    private Set<AttackType> immunities;
    private int units;
    private int groupNumber;
    private int groupIdWhichIAttack;

    public Group(int hitPoints, int attackDamage, AttackType attackType, int initiative, Set<AttackType> immunities, Set<AttackType> weaknesses, int units) {
        this.hitPoints = hitPoints;
        this.attackDamage = attackDamage;
        this.attackType = attackType;
        this.initiative = initiative;
        this.weaknesses = weaknesses;
        this.immunities = immunities;
        this.units = units;
        this.groupNumber = groupCounter++;
        this.groupIdWhichIAttack = -1;
    }

    public int getEffectivePower() {
        return units * attackDamage;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public AttackType getAttackType() {
        return attackType;
    }

    public int getInitiative() {
        return initiative;
    }

    public Set<AttackType> getWeaknesses() {
        return weaknesses;
    }

    public Set<AttackType> getImmunities() {
        return immunities;
    }

    public int getUnits() {
        return units;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public int getGroupIdWhichIAttack() {
        return groupIdWhichIAttack;
    }

    public void setGroupIdWhichIAttack(int groupIdWhichIAttack) {
        this.groupIdWhichIAttack = groupIdWhichIAttack;
    }

    public int calculateDamage(Group aggressor) {
        if (this.immunities.contains(aggressor.getAttackType())){
            return 0;
        } else if (this.weaknesses.contains(aggressor.getAttackType())) {
            return aggressor.getEffectivePower() * 2;
        } else {
            return aggressor.getEffectivePower();
        }
    }

    public boolean getAttack(Group aggressor) {
        units -= (int) Math.floor(calculateDamage(aggressor) / hitPoints);
        units = Math.max(units, 0);
        return units == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return hitPoints == group.hitPoints && attackDamage == group.attackDamage && initiative == group.initiative && units == group.units && attackType == group.attackType && weaknesses.equals(group.weaknesses) && immunities.equals(group.immunities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hitPoints, attackDamage, attackType, initiative, weaknesses, immunities, units);
    }

    @Override
    public int compareTo(Group o) {
        return o.getInitiative() - this.initiative;
    }

}
