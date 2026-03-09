import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final Team team = new Team();
    private static final BattleSystem battle = new BattleSystem(team, sc);
    private static final Storyline story = new Storyline(sc);

    public static void main(String[] args) {
        intro();
        boolean cont;

        cont = stage(1);
        if (!cont) gameOver("You did not survive Stage 1.");

        cont = stage(2);
        if (!cont) gameOver("You did not survive Stage 2.");

        cont = stage(3);
        if (!cont) gameOver("You did not survive Stage 3.");

        cont = stage(4);
        if (!cont) gameOver("You did not survive Stage 4.");

        cont = stage5();
        if (!cont) gameOver("You failed at the final challenge.");

        conclude();
    }

    private static void intro() {
        System.out.println("=== THE NEXT STEP: Dr. RICO's Experiment ===");
        System.out.println("Year 2098. A secret facility in ruins.");
        System.out.println("At Abistado College of Technology, three students—Mah, Rey, and Tess—are drawn into an underground experiment.");
        System.out.println("A recorded voice echoes through the halls: \"Survive my test or become part of it.\"");
        System.out.println("Press Enter to begin...");
        sc.nextLine();
    }

    private static boolean stage(int n) {
        switch (n) {
            case 1:
                System.out.println("\n--- Stage 1: Entrance Chamber ---");
                break;
            case 2:
                System.out.println("\n--- Stage 2: Library of Echoes ---");
                break;
            case 3:
                System.out.println("\n--- Stage 3: Training Hall ---");
                break;
            case 4:
                System.out.println("\n--- Stage 4: Observation Wing ---");
                break;
            default:
                System.out.println("\n--- Stage ---");
        }

        boolean logical = story.presentPuzzle(stageTitle(n), n);
        if (logical) {
            System.out.println("You solved the puzzle correctly — no zombies appear.");
            team.resetCooldowns();
        } else {
            System.out.println("Wrong choice — zombies awaken!");
            battle.startFightRandom();
            team.resetCooldowns();
            if (!team.isAnyAlive()) return false;
        }

        story.presentScene(n);
        return team.isAnyAlive();
    }

    private static String stageTitle(int n) {
        return switch (n) {
            case 1 -> "Entrance Chamber";
            case 2 -> "Library of Echoes";
            case 3 -> "Training Hall";
            case 4 -> "Observation Wing";
            default -> "Stage";
        };
    }

    private static boolean stage5() {
        System.out.println("\n--- STAGE 5: The Heart of the Experiment ---");
        team.showStatus();
        boolean finalChoiceGood = story.presentPuzzle("Final Console", 5);
        boolean alerted = story.wasFinalPuzzleNonLogical();
        if (alerted) {
            System.out.println("Your choice alerted defenses. Dr. RICO's chamber activates.");
        } else {
            System.out.println("You acted carefully. Proceed to the confrontation.");
        }
        Zombie boss = new Zombie("Dr. RICO", 350, 30);
        boolean survived = battle.bossFight(boss);
        team.resetCooldowns();
        if (!survived) return false;
        if (finalChoiceGood && team.isAnyAlive()) {
            System.out.println("\n--- GOOD ENDING ---");
            System.out.println("You sabotaged the experiment and escape. The truth is exposed.");
            story.revealChoicesOutcome(true);
            team.showFinalStatus();
        } else {
            System.out.println("\n--- BAD ENDING ---");
            System.out.println("Dr. RICO's plan persists. You leave the facility changed by the choice.");
            story.revealChoicesOutcome(false);
            team.showFinalStatus();
        }
        return true;
    }

    private static void conclude() {
        System.out.println("The run has ended. Thank you for playing.");
        System.exit(0);
    }

    private static void gameOver(String reason) {
        System.out.println("\n+++ GAME OVER +++");
        System.out.println(reason);
        story.revealChoicesOutcome(false);
        team.showFinalStatus();
        System.exit(0);
    }
}
