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
                return "Test Paměti";
            case da:
                return "Hukommelse spil";
            case de:
                return "Memory Spiel";
            case el:
                return "Δοκιμή μνήμης";
            case en:
                return "Memory Exam";
            case es:
                return "Prueba de Memoria";
            case fi:
                return "Muisti testi";
            case fr:
                return "Examen de Mémoire";
            case hi:
                return "दिमागी परीक्षा";
            case hr:
                return "Igra Pamćenja";
            case hu:
                return "Memória Teszt";
            case id:
                return "Tes Memori";
            case it:
                return "Test di Memoria";
            case ja:
                return "記憶テスト";
            case ko:
                return "메모리 검사";
            case ms:
                return "Ujian Memori";
            case nl:
                return "Geheim examen";
            case no:
                return "Minne spill";
            case pl:
                return "Test Pamięci";
            case pt:
                return "Teste de Memória";
            case ro:
                return "Joc de Memorie";
            case ru:
                return "Тест памяти";
            case sk:
                return "Pamäťová Hra";
            case sv:
                return "Minnes test";
            case th:
                return "เกมความจำ";
            case tr:
                return "Hafıza Sınavı";
            case uk:
                return "Гра на пам'ять";
            case vi:
                return "Kiểm tra bộ nhớ";
            case zh:
                return "记忆考试";
        }
        return null;
    }
}
