package libgdx.startgame;

import libgdx.constants.Language;
import libgdx.game.Game;
import libgdx.implementations.SkelClassicGameIdEnum;
import libgdx.implementations.imagesplit.ImageSplitGame;
import libgdx.utils.startgame.test.DefaultAppInfoService;

public class ImageSplitStartGame {

    public static void main(String[] args) {
        ImageSplitGame game = new ImageSplitGame(
                new DefaultAppInfoService() {
                    @Override
                    public String getGameIdPrefix() {
                        return SkelClassicGameIdEnum.imagesplit.name();
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
                return "Foto Puzzle";
            case da:
                return "Foto Puslespil";
            case de:
                return "Fotopuzzle";
            case el:
                return "Παζλ φωτογραφιών";
            case en:
                return "Photo Puzzle Game";
            case es:
                return "Rompecabezas de Fotos";
            case fi:
                return "Valokuvapulma";
            case fr:
                return "Puzzle Photo";
            case hi:
                return "फोटो पहेली";
            case hr:
                return "Foto Zagonetka";
            case hu:
                return "Fotó Puzzle";
            case id:
                return "Teka-teki Foto";
            case it:
                return "Puzzle di fotografia";
            case ja:
                return "フォトパズルゲーム";
            case ko:
                return "사진 퍼즐 게임";
            case ms:
                return "Teka-teki Gambar";
            case nl:
                return "Foto Puzzelspel";
            case no:
                return "Fotopuslespill";
            case pl:
                return "Puzzle ze Zdjęciem";
            case pt:
                return "Quebra-cabeça de Fotos";
            case ro:
                return "Puzzle cu Fotografii";
            case ru:
                return "Фото пазл";
            case sk:
                return "Foto Puzzle";
            case sv:
                return "Fotopussel";
            case th:
                return "ปริศนาภาพ";
            case tr:
                return "Fotoğraf Bulmaca";
            case uk:
                return "Фото головоломка";
            case vi:
                return "Câu đố ảnh";
            case zh:
                return "照片逻辑游戏";
        }
        return null;
    }
}
