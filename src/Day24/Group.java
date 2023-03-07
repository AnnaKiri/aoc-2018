package Day24;

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
    private int id;
    private int myVictimId;
    private Group myAggressor;

    public Group(int hitPoints, int attackDamage, AttackType attackType, int initiative, Set<AttackType> immunities, Set<AttackType> weaknesses, int units) {
        this.hitPoints = hitPoints;
        this.attackDamage = attackDamage;
        this.attackType = attackType;
        this.initiative = initiative;
        this.weaknesses = weaknesses;
        this.immunities = immunities;
        this.units = units;
        this.id = groupCounter++;
        this.myVictimId = -1;
        this.myAggressor = null;
    }

    public int getEffectivePower() {
        return units * attackDamage;
    }

    public AttackType getAttackType() {
        return attackType;
    }

    public int getInitiative() {
        return initiative;
    }

    public int getUnits() {
        return units;
    }

    public int getId() {
        return id;
    }

    public int getMyVictimId() {
        return myVictimId;
    }

    public void setMyVictimId(int myVictimId) {
        this.myVictimId = myVictimId;
    }

    public void setMyAggressor(Group myAggressor) {
        this.myAggressor = myAggressor;
    }

    public int calculateDamage() {
        if (myAggressor == null) {
            return 0;
        }
        if (this.immunities.contains(myAggressor.getAttackType())){
            return 0;
        } else if (this.weaknesses.contains(myAggressor.getAttackType())) {
            return myAggressor.getEffectivePower() * 2;
        } else {
            return myAggressor.getEffectivePower();
        }
    }

    public boolean getAttack() {
        units -= (int) Math.floor(calculateDamage() / hitPoints);
        units = Math.max(units, 0);
        return units == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return hitPoints == group.hitPoints
                && attackDamage == group.attackDamage
                && initiative == group.initiative
                && units == group.units
                && id == group.id
                && myVictimId == group.myVictimId
                && attackType == group.attackType
                && weaknesses.equals(group.weaknesses)
                && immunities.equals(group.immunities)
                && myAggressor.equals(group.myAggressor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hitPoints, attackDamage, attackType, initiative, weaknesses, immunities, units, id, myVictimId, myAggressor);
    }

    @Override
    public int compareTo(Group o) {
        return o.getInitiative() - this.initiative;
    }

}
