package libgdx.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.SkelClassicGameIdEnum;
import libgdx.implementations.buylow.BuyLowGame;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class BuyLowStartGame {

    public static void main(String[] args) {
        BuyLowGame game = new BuyLowGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return SkelClassicGameIdEnum.buylow.name();
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
                        return "en";
                    }
                });
        libgdx.utils.startgame.StartGame.main(game, args);
    }

    public static String getTitle() {
        switch (Language.valueOf(Game.getInstance().getAppInfoService().getLanguage())) {
            case cs:
                return "Zisková hra";
            case da:
                return "Profit spil";
            case de:
                return "Profit machen!";
            case el:
                return "Παιχνίδι κέρδους";
            case en:
                return "Profit Game";
            case es:
                return "Juego de ganancias";
            case fi:
                return "Voittopeli";
            case fr:
                return "Jeu de profit";
            case hi:
                return "लाभ का खेल";
            case hr:
                return "Dobitna igra";
            case hu:
                return "Profit játék";
            case id:
                return "Game untung";
            case it:
                return "Gioco di profitto";
            case ja:
                return "利益ゲーム";
            case ko:
                return "이익 게임";
            case ms:
                return "Permainan untung";
            case nl:
                return "Winst spel";
            case no:
                return "Profitt spill";
            case pl:
                return "Gra zysku";
            case pt:
                return "Jogo de lucro";
            case ro:
                return "Joc de profit";
            case ru:
                return "Прибыльная игра";
            case sk:
                return "Zisková hra";
            case sv:
                return "Vinstspel";
            case th:
                return "เกมกำไร";
            case tr:
                return "Kar oyunu";
            case uk:
                return "Прибуткова гра";
            case vi:
                return "Trò chơi lợi nhuận";
            case zh:
                return "利润游戏";
        }
        return null;
    }
}
