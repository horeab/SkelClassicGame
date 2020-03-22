package libgdx.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.GameIdEnum;
import libgdx.implementations.memory.MemoryGame;
import libgdx.implementations.resourcewars.ResourceWarsGame;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class ResourceWarsStartGame {

    public static void main(String[] args) {

        /**
         *  //TODO change GAME TYPE
         *  //TODO change GAME TYPE
         */
        ResourceWarsGame game = new ResourceWarsGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return GameIdEnum.resourcewars.name();
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
                        return "el";
                    }
                });
        libgdx.utils.startgame.StartGame.main(game, args);
    }

    public static String getTitle() {
        switch (Language.valueOf(Game.getInstance().getAppInfoService().getLanguage())) {
            case cs:
                return "Obchodní Hra";
            case da:
                return "Forretnings spil";
            case de:
                return "Geschäfts spiel";
            case el:
                return "Επιχείρηση παιχνίδι";
            case en:
                return "Business Game";
            case es:
                return "Juego de Negocios";
            case fi:
                return "Liiketoiminta peli";
            case fr:
                return "Jeu d'affaires";
            case hi:
                return "बिजनेस गेम";
            case hr:
                return "Poslovna Igra";
            case hu:
                return "Üzleti Játék";
            case id:
                return "Game Bisnis";
            case it:
                return "Gioco Aziendale";
            case ja:
                return "ビジネスゲーム";
            case ko:
                return "비즈니스 게임";
            case ms:
                return "Permainan Perniagaan";
            case nl:
                return "Zakelijk Spel";
            case no:
                return "Forretnings spill";
            case pl:
                return "Gra Biznesowa";
            case pt:
                return "Jogo de Negócios";
            case ro:
                return "Joc de Afaceri";
            case ru:
                return "Деловая игра";
            case sk:
                return "Obchodná Hra";
            case sv:
                return " Affärs spel ";
            case th:
                return "เกมธุรกิจ";
            case tr:
                return "İş Oyunu";
            case uk:
                return "Ділова гра";
            case vi:
                return "Trò chơi kinh doanh";
            case zh:
                return "商业游戏";
        }
        return null;
    }
}
