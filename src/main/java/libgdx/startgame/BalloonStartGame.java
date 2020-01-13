package libgdx.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.GameIdEnum;
import libgdx.implementations.balloon.BalloonGame;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class BalloonStartGame {

    public static void main(String[] args) {
        BalloonGame game = new BalloonGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return GameIdEnum.balloon.name();
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
                    public boolean isPortraitMode() {
                        return false;
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
                return "Balónkové Puzzle";
            case da:
                return "Balloon Puzzle";
            case de:
                return "Ballon Puzzle";
            case el:
                return "Μπαλόνι Παζλ";
            case en:
                return "Puzzle Balloon";
            case es:
                return "Globos Puzzle";
            case fi:
                return "Ilmapallo Palapeli";
            case fr:
                return "Puzzle Ballon";
            case hi:
                return "गुब्बारा पहेली";
            case hr:
                return "Balon Puzzle";
            case hu:
                return "Léggömb Puzzle";
            case id:
                return "Puzzle Balon";
            case it:
                return "Palloncino Puzzle";
            case ja:
                return "風船パズル";
            case ko:
                return "풍선";
            case ms:
                return "Teka-teki Belon";
            case nl:
                return "Ballon Puzzel";
            case no:
                return "Ballong Puzzle";
            case pl:
                return "Balonowe Puzzle";
            case pt:
                return "Balão Puzzle";
            case ro:
                return "Puzzle Baloane";
            case ru:
                return "Надувные шарики";
            case sk:
                return "Balónkové Puzzle";
            case sv:
                return "Ballongpussel";
            case th:
                return "ปริศนาบอลลูน";
            case tr:
                return "Balon Bulmaca";
            case uk:
                return "Повітряні кулі";
            case vi:
                return "Bong bóng câu đố";
            case zh:
                return "气球难题";
        }
        return null;
    }
}
