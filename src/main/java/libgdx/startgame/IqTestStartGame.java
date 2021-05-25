package libgdx.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.SkelClassicGameIdEnum;
import libgdx.implementations.iqtest.IqTestGame;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class IqTestStartGame {

    public static void main(String[] args) {
        IqTestGame game = new IqTestGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return SkelClassicGameIdEnum.iqtest.name();
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
                        return true;
                    }

                    @Override
                    public String getLanguage() {
                        return Language.en.name();
                    }
                });
        libgdx.utils.startgame.StartGame.main(game, args);
    }

    public static String getTitle() {
        switch (Language.valueOf(Game.getInstance().getAppInfoService().getLanguage())) {
            case cs:
                return "Zpravodajský Test";
            case da:
                return "Intelligens Test";
            case de:
                return "Intelligenztest";
            case el:
                return "Δοκιμή νοημοσύνης";
            case en:
                return "Intelligence Test";
            case es:
                return "Prueba de inteligencia";
            case fi:
                return "Älykkyystesti";
            case fr:
                return "Test d'intelligence";
            case hi:
                return "खुफिया परीक्षण";
            case hr:
                return "Test inteligencije";
            case hu:
                return "Intelligencia Teszt";
            case id:
                return "Tes Kecerdasan";
            case it:
                return "Test di intelligenza";
            case ja:
                return "知能テスト";
            case ko:
                return "지능 검사";
            case ms:
                return "Ujian Perisikan";
            case nl:
                return "Intelligentie Test";
            case no:
                return "Intelligens Test";
            case pl:
                return "Test na inteligencje";
            case pt:
                return "Teste de inteligência";
            case ro:
                return "Test de inteligență";
            case ru:
                return "Тест на интеллект";
            case sk:
                return "Spravodajský test";
            case sv:
                return "Intelligenstest";
            case th:
                return "ทดสอบสติปัญญา";
            case tr:
                return "Dahi Testi";
            case uk:
                return "Тест інтелекту";
            case vi:
                return "Bài kiểm tra trí thông minh";
            case zh:
                return "智力测验";
        }
        return null;
    }
}
