package libgdx.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.GameIdEnum;
import libgdx.implementations.applepie.ApplePieGame;
import libgdx.implementations.math.MathGame;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class ApplePieStartGame {

    public static void main(String[] args) {
        ApplePieGame game = new ApplePieGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return GameIdEnum.applepie.name();
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
                        return "ro";
                    }
                });
        libgdx.utils.startgame.StartGame.main(game, args);
    }

    public static String getTitle() {
        switch (Language.valueOf(Game.getInstance().getAppInfoService().getLanguage())) {
            case cs:
                return "Matematika";
            case da:
                return "Matematik";
            case de:
                return "Mathematik";
            case el:
                return "Μαθηματικά";
            case en:
                return "Math Test";
            case es:
                return "Matemáticas";
            case fi:
                return "Matematiikka";
            case fr:
                return "Mathématiques";
            case hi:
                return "गणित";
            case hr:
                return "Matematika";
            case hu:
                return "Matematika";
            case id:
                return "Matematika";
            case it:
                return "Test di matematica";
            case ja:
                return "数学";
            case ko:
                return "수학";
            case ms:
                return "Matematik";
            case nl:
                return "Wiskunde";
            case no:
                return "Matte";
            case pl:
                return "Matematyka";
            case pt:
                return "Matemática";
            case ro:
                return "Matematică";
            case ru:
                return "Математика";
            case sk:
                return "Matematika";
            case sv:
                return "Matematik";
            case th:
                return "คณิตศาสตร์";
            case tr:
                return "Matematik";
            case uk:
                return "Математика";
            case vi:
                return "Toán học";
            case zh:
                return "数学";
        }
        return null;
    }
}
