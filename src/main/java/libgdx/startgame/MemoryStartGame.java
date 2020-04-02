package libgdx.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.GameIdEnum;
import libgdx.implementations.math.MathGame;
import libgdx.implementations.memory.MemoryGame;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class MemoryStartGame {

    public static void main(String[] args) {
        MemoryGame game = new MemoryGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return GameIdEnum.memory.name();
                    }

                    @Override
                    public float gameScreenTopMargin() {
                        return super.gameScreenTopMargin();
                    }

                    @Override
                    public String getAppName() {
                        return getTitle();
                    }

                    @Override
                    public String getLanguage() {
                        return "cs";
                    }
                });
        libgdx.utils.startgame.StartGame.main(game, args);
    }

    public static String getTitle() {
        switch (Language.valueOf(Game.getInstance().getAppInfoService().getLanguage())) {
            case cs:
                return "Objevte pár!";
            case da:
                return "Oplev parret!";
            case de:
                return "Entdecke das Paar!";
            case el:
                return "Ανακαλύψτε το ζευγάρι!";
            case en:
                return "Discover the pair!";
            case es:
                return "¡Descubre la pareja!";
            case fi:
                return "Löydä pari!";
            case fr:
                return "Découvrez la paire!";
            case hi:
                return "जोड़ी की खोज!";
            case hr:
                return "Otkrijte par!";
            case hu:
                return "Fedezze fel a párot!";
            case id:
                return "Temukan pasangan!";
            case it:
                return "Scopri la coppia!";
            case ja:
                return "ペアを発見！";
            case ko:
                return "쌍을 발견하십시오!";
            case ms:
                return "Temui pasangan itu!";
            case nl:
                return "Ontdek het paar!";
            case no:
                return "Oppdag paret!";
            case pl:
                return "Odkryj parę!";
            case pt:
                return "Descubra o par!";
            case ro:
                return "Descoperă perechea!";
            case ru:
                return "Откройте для себя пару!";
            case sk:
                return "Objavte pár!";
            case sv:
                return "Upptäck paret!";
            case th:
                return "ค้นพบคู่!";
            case tr:
                return "Çifti keşfedin!";
            case uk:
                return "Відкрийте для себе пару!";
            case vi:
                return "Khám phá cặp!";
            case zh:
                return "发现一对！";
        }
        return null;
    }
}
