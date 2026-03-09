import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BattleSystem {

    private final Team team;
    private final Scanner sc;
    private final Random rand = new Random();

    public BattleSystem(Team team, Scanner sc) {
        this.team = team;
        this.sc = sc;
    }

    public void startFightRandom() {
        int count = 1 + rand.nextInt(3);
        List<Zombie> zombies = new ArrayList<>();
        for (int i = 0; i < count; i++) zombies.add(new Zombie("Zombie " + (i + 1)));
        fight(zombies);
    }

    public boolean bossFight(Zombie boss) {
        List<Zombie> single = new ArrayList<>();
        single.add(boss);
        fight(single);
        return team.isAnyAlive();
    }

    private void fight(List<Zombie> zombies) {
        int turn = 1;
        while (team.isAnyAlive() && anyAlive(zombies)) {
            System.out.println("\n--- TURN " + turn + " ---");
            team.showStatus();
            showZombies(zombies);
            team.reduceCooldowns();
            Character actor = team.chooseCharacter(sc);
            actor.act(sc, zombies, team.getInventory());
            removeDead(zombies);
            if (anyAlive(zombies)) zombiesAttack(zombies);
            removeDead(zombies);
            turn++;
        }
    }

    private void zombiesAttack(List<Zombie> zombies) {
        System.out.println("Zombies attack!");
        for (Zombie z : zombies) {
            if (!z.isAlive()) continue;
            Character target = team.getRandomAliveMember();
            int dmg = z.attack();
            target.takeDamage(dmg);
            System.out.println(z.getName() + " deals " + dmg + " damage to " + target.getName());
            if (!team.isAnyAlive()) break;
        }
    }

    private boolean anyAlive(List<Zombie> list) {
        for (Zombie z : list) if (z.isAlive()) return true;
        return false;
    }

    private void removeDead(List<Zombie> list) {
        list.removeIf(z -> !z.isAlive());
    }

    private void showZombies(List<Zombie> zombies) {
        for (int i = 0; i < zombies.size(); i++) {
            Zombie z = zombies.get(i);
            System.out.println((i + 1) + ") " + z.getName() + " HP: " + z.getHP());
        }
    }
}
