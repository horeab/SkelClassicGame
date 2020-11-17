package libgdx.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.GameIdEnum;
import libgdx.implementations.imagesplit.ImageSplitGame;
import libgdx.implementations.kidlearn.KidLearnGame;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class KidLearnStartGame {

    public static void main(String[] args) {
        KidLearnGame game = new KidLearnGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return GameIdEnum.kidlearn.name();
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
                        return Language.en.name();
                    }

                    @Override
                    public boolean isPortraitMode() {
                        return false;
                    }
                });
        libgdx.utils.startgame.StartGame.main(game, args);
    }

    public static String getTitle() {
        switch (Language.valueOf(Game.getInstance().getAppInfoService().getLanguage())) {
            case cs:
                return "Vzdělávací hry";
            case da:
                return "Uddannelsesspil";
            case de:
                return "Lernspiel für Kinder";
            case el:
                return "Παιχνίδια εκμάθησης";
            case en:
                return "Educational Games";
            case es:
                return "Juegos educacionales";
            case fi:
                return "Koulutuspelit";
            case fr:
                return "Jeux éducatifs";
            case hi:
                return "शैक्षिक खेल";
            case hr:
                return "Obrazovne igre";
            case hu:
                return "Oktatási játékok";
            case id:
                return "Game Edukasi";
            case it:
                return "Giochi di apprendimento";
            case ja:
                return "教育ゲーム";
            case ko:
                return "교육용 게임";
            case ms:
                return "Permainan Pembelajaran";
            case nl:
                return "Educatieve spellen";
            case no:
                return "Pedagogiske spill";
            case pl:
                return "Gry edukacyjne";
            case pt:
                return "Jogo Educativo";
            case ro:
                return "Jocuri educaționale";
            case ru:
                return "Развивающая игра";
            case sk:
                return "Vzdelávacie hry";
            case sv:
                return "Pedagogiskt spel";
            case th:
                return "เกมการศึกษา";
            case tr:
                return "Eğitici oyunlar";
            case uk:
                return "Навчальні ігри";
            case vi:
                return "Trò chơi giáo dục";
            case zh:
                return "教育游戏";
        }
        return null;
    }
}
