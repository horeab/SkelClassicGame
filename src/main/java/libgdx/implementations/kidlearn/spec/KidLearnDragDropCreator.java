package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSize;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.resources.FontManager;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.SoundUtils;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public abstract class KidLearnDragDropCreator extends KidLearnGameCreator {

    private static final float OPT_MOVE_DURATION = 0.2f;
    protected static final float UNK_FADE_DURATION = 0.1f;
    public static final String IMG_TEXT_STACK_IMAGE = "ImgTextStackIMAGE";
    protected MyButton verifyBtn;
    protected List<KidLearnImgInfo> unknownImg = new ArrayList<>();
    protected List<KidLearnImgInfo> optionsImg = new ArrayList<>();
    protected List<KidLearnImgInfo> alreadyFilledUnknownImg = new ArrayList<>();
    protected List<KidLearnImgInfo> alreadyMovedOptionImg = new ArrayList<>();


    public KidLearnDragDropCreator(KidLearnGameContext gameContext) {
        super(gameContext);
    }

    protected abstract boolean isResponseCorrect();

    protected abstract int getTotalOptions();

    protected abstract List<Pair<String, Res>> getAllOptions();

    protected abstract int getTotalItems();

    protected abstract double getNumberOfCorrectUnknownItems();

    protected abstract void createAllItemsContainer();

    protected abstract void sortAlreadyMovedOptionImg();

    protected abstract Pair<Float, Float> getCoordsForOptionRow(int index);

    protected abstract Pair<Float, Float> getCoordsForResponseRow(int index);

    public void create() {
        super.create();
        createAllItemsContainer();
        createResetBtn();
        createOptionsContainer();
        if (!kidLearnPreferencesManager.isTutorialPlayed(getTutorialLevelName())) {
            tutorialFinger();
            kidLearnPreferencesManager.putTutorialPlayed(getTutorialLevelName());
        }
    }

    private String getTutorialLevelName() {
        return gameContext.level.getClass().getSimpleName();
    }

    protected void tutorialFinger() {
        int index = (int) Math.ceil(unknownImg.size() / 2f);
        KidLearnImgInfo centerResponse1 = unknownImg.get(index);
        KidLearnImgInfo centerResponse2 = unknownImg.get(index - 1);
        float initialDelayDuration = 0.5f;
        float moveDuration = 0.75f;
        float fadeOutDuration = 0.5f;
        tutorialFinger(centerResponse1, initialDelayDuration, moveDuration, fadeOutDuration);
        afterFirstTutorial(centerResponse2, initialDelayDuration, moveDuration, fadeOutDuration);
    }

    protected void afterFirstTutorial(final KidLearnImgInfo centerResponse2, final float initialDelayDuration, final float moveDuration, final float fadeOutDuration) {
        screen.addAction(Actions.sequence(Actions.delay(initialDelayDuration + moveDuration + fadeOutDuration, Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                tutorialFinger(centerResponse2, initialDelayDuration, moveDuration, fadeOutDuration);
            }
        }))));
    }

    private void tutorialFinger(KidLearnImgInfo response, float initialDelayDuration, float moveDuration, float fadeOutDuration) {
        KidLearnImgInfo centerOption = optionsImg.get(optionsImg.size() / 2);
        Image finger = GraphicUtils.getImage(KidLearnSpecificResource.tutorial_finger);
        finger.setHeight(getOptionWidth() / 2);
        finger.setWidth(getOptionWidth() / 2);
        finger.setX(centerOption.initialCoord.getLeft() + getOptionWidth() / 2);
        finger.setY(centerOption.initialCoord.getRight());
        addActorToScreen(finger);
        finger.addAction(Actions.sequence(Actions.delay(initialDelayDuration), Actions.moveTo(response.initialCoord.getLeft() + getOptionWidth() / 2,
                finger.getY() + ScreenDimensionsManager.getScreenHeightValue(20), moveDuration), Actions.fadeOut(fadeOutDuration)));
    }

    protected Table addOptionImg(Pair<Float, Float> coord, Res res, String text) {
        String labelName = "OPTION_LABEL" + text;
        float optionHeight = getOptionHeight();
        float optionWidth = getOptionWidth();
        Table img = addImg(coord, res, optionWidth, optionHeight, labelName, text);
        processOptionTextLabel((MyWrappedLabel) img.findActor(labelName));
        img.setWidth(optionWidth);
        img.setHeight(optionHeight);
        return img;
    }

    protected Table addResponseImg(Pair<Float, Float> coord, Res res, String text) {
        String labelName = "RESPONSE_LABEL" + text;
        float responseHeight = getResponseHeight();
        float responseWidth = getResponseWidth();
        Table img = addImg(coord, res, responseWidth, responseHeight, labelName, text);
        processResponseTextLabel((MyWrappedLabel) img.findActor(labelName));
        img.setWidth(responseWidth);
        img.setHeight(responseHeight);
        return img;
    }

    private Table addImg(Pair<Float, Float> coord, Res res, float imgWidth, float imgHeight, String labelName, String text) {
        Stack img = createImgTextStack(text, imgWidth, labelName, res);
        Table table = new Table();
        table.add(img).width(imgWidth).height(imgHeight);
        table.setX(coord.getLeft());
        table.setY(coord.getRight());
        addActorToScreen(table);
        return table;
    }

    protected void createResetBtn() {
        ButtonSize btnSize = SkelClassicButtonSize.KIDLEARN_RESET;
        MyButton btn = new ImageButtonBuilder(MainButtonSkin.REFRESH, screen)
                .setFixedButtonSize(SkelClassicButtonSize.KIDLEARN_RESET)
                .build();
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                executeReset();
            }
        });
        btn.setX(ScreenDimensionsManager.getScreenWidth() - btnSize.getWidth() - MainDimen.horizontal_general_margin.getDimen());
        btn.setY(MainDimen.vertical_general_margin.getDimen());
        addActorToScreen(btn);
    }

    private void executeReset() {
        if (verifyBtn != null) {
            verifyBtn.setVisible(false);
        }
        alreadyFilledUnknownImg.clear();
        alreadyMovedOptionImg.clear();
        for (KidLearnImgInfo unkInfo : unknownImg) {
            unkInfo.img.addAction(Actions.fadeIn(UNK_FADE_DURATION));
            executeOptionResetAnimation(unkInfo.img);
        }
        for (KidLearnImgInfo learnImgInfo : optionsImg) {
            Pair<Float, Float> initialCoord = learnImgInfo.initialCoord;
            learnImgInfo.img.addAction(Actions.moveTo(initialCoord.getLeft(), initialCoord.getRight(), OPT_MOVE_DURATION));
            learnImgInfo.img.setTouchable(Touchable.enabled);
            executeOptionResetAnimation(learnImgInfo.img);
        }
    }

    protected void executeResponseResetAnimation(Table img) {
    }

    protected void executeOptionResetAnimation(Table img) {
    }

    protected void executeOnDragStart(KidLearnImgInfo opt) {
    }

    private void createOptionsContainer() {
        List<Pair<String, Res>> allOptions = new ArrayList<>(getAllOptions());
        Collections.shuffle(allOptions);
        for (Pair<String, Res> option : allOptions) {
            int itemsAlreadyAdded = optionsImg.size();
            final Pair<Float, Float> coord = getCoordsForOptionRow(itemsAlreadyAdded);
            final Table img = addOptionImg(coord, option.getRight(), option.getLeft());
            final KidLearnImgInfo opt = new KidLearnImgInfo(coord, img, option.getLeft());
            optionsImg.add(opt);
            img.addListener(new DragListener() {

                @Override
                public void dragStart(InputEvent event, float x, float y, int pointer) {
                    if (!alreadyMovedOptionImg.contains(opt)) {
                        executeOnDragStart(opt);
                    }
                }

                @Override
                public void drag(InputEvent event, float x, float y, int pointer) {
                    if (!alreadyMovedOptionImg.contains(opt)) {
                        img.toFront();
                        img.moveBy(x - img.getWidth() / 2, y - img.getHeight() / 2);
                    }
                }

                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer) {
                    float responseSideDimenWidth = getResponseWidth();
                    float responseSideDimenHeight = getResponseHeight();
                    float optionWidth = getOptionWidth();
                    float optionHeight = getOptionHeight();
                    float acceptedDistWidth = getAcceptedDistanceForDropWidth();
                    float acceptedDistHeight = getAcceptedDistanceForDropHeight();
                    boolean noMatch = true;
                    for (KidLearnImgInfo unkInfo : unknownImg) {
                        Table unk = unkInfo.img;
                        if ((unk.getX() - acceptedDistWidth < img.getX() && unk.getX() + responseSideDimenWidth + acceptedDistWidth > (img.getX() + optionWidth))
                                &&
                                (unk.getY() - acceptedDistHeight < img.getY() && unk.getY() + responseSideDimenHeight + acceptedDistHeight > (img.getY() + optionHeight))
                                && optionDragStopValidExtraCondition(unkInfo)
                                ) {
                            opt.img.addAction(Actions.moveTo(dragStopMoveToX(unk), dragStopMoveToY(unk), 0.3f));
                            noMatch = false;
                            opt.img.setTouchable(Touchable.disabled);
                            alreadyMovedOptionImg.add(opt);
                            alreadyFilledUnknownImg.add(unkInfo);
                            executeAnimationAfterDragStop(opt.img, unkInfo.img);
                        }
                    }
                    if (noMatch) {
                        img.addAction(Actions.moveTo(coord.getLeft(), coord.getRight(), OPT_MOVE_DURATION));
                    } else if (alreadyMovedOptionImg.size() == getNumberOfCorrectUnknownItems()) {
                        createVerifyBtn();
                    }
                }
            });
        }
    }

    protected float dragStopMoveToY(Table unk) {
        return unk.getY();
    }

    protected float dragStopMoveToX(Table unk) {
        return unk.getX();
    }

    protected boolean optionDragStopValidExtraCondition(KidLearnImgInfo unkInfo) {
        return !alreadyFilledUnknownImg.contains(unkInfo);
    }

    protected void executeAnimationAfterDragStop(Table opt, Table unk) {
        unk.addAction(Actions.sequence(new AlphaAction[]{Actions.fadeOut(UNK_FADE_DURATION)}));
    }

    protected Table getLastAddedImgInContainer(float containerX) {
        Table res = null;
        for (KidLearnImgInfo info : alreadyMovedOptionImg) {
            if (info.img.getX() == containerX) {
                res = info.img;
            }
        }
        return res;
    }

    void createVerifyBtn() {
        for (KidLearnImgInfo learnImgInfo : optionsImg) {
            learnImgInfo.img.setTouchable(Touchable.disabled);
        }
        if (verifyBtn == null) {
            ButtonSize btnSize = SkelClassicButtonSize.KIDLEARN_VERIFY;
            verifyBtn = new ButtonBuilder(KidLearnGameLabel.l_verify.getText())
                    .setFontScale(FontManager.getBigFontDim())
                    .setButtonSkin(SkelClassicButtonSkin.KIDLEARN_VERIFY)
                    .setFixedButtonSize(btnSize)
                    .build();
            verifyBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!isResponseCorrect()) {
                        SoundUtils.playSound(KidLearnSpecificResource.level_fail);
                        executeReset();
                    } else {
                        verifyBtn.setTouchable(Touchable.disabled);
                        verifyBtn.addAction(Actions.fadeOut(0.2f));
                        executeCorrectAnswer();
                    }
                }
            });
            verifyBtn.setX(getVerifyBtnX());
            verifyBtn.setY(getVerifyBtnY());
            addActorToScreen(verifyBtn);
        } else {
            verifyBtn.setVisible(true);
        }
        verifyBtn.toFront();
    }

    private Stack createImgTextStack(String text, float labelWidth, String labelName, Res res) {
        Stack stack = new Stack();
        Image image = GraphicUtils.getImage(res);
        image.setName(IMG_TEXT_STACK_IMAGE);
        stack.add(image);
        MyWrappedLabelConfigBuilder builder = new MyWrappedLabelConfigBuilder()
                .setWidth(labelWidth)
                .setFontConfig(getImgStackTextFontConfig(FontConfig.FONT_SIZE / getOptionTextStandardFontSize(text))).setText(StringUtils.capitalize(text));
        boolean isLongText = text.length() >= 10 && text.contains(" ");
        if (isLongText) {
            builder.setWrappedLineLabel(labelWidth);
            float mult = 1f;
            if (text.length() >= 15) {
                mult = 1.4f;
            } else if (text.length() >= 14) {
                mult = 1.3f;
            } else if (text.length() >= 13) {
                mult = 1.2f;
            } else if (text.length() >= 12) {
                mult = 1.1f;
            }
            builder.setFontConfig(getImgStackTextFontConfig(FontConfig.FONT_SIZE / mult));
        }
        MyWrappedLabel textLabel = new MyWrappedLabel(builder.build());
        if (!isLongText) {
            textLabel = textLabel.fitToContainer();
        }
        Table table = new Table();
        Table labelTable = new Table();
        processTextTableBeforeAddText(table);
        table.add(labelTable);
        processLabelTable(labelTable, text);
        labelTable.add(textLabel);
        textLabel.setName(labelName);
        stack.add(table);
        table.toFront();
        return stack;
    }

    protected void processLabelTable(Table labelTable, String text) {
    }

    protected float getOptionTextStandardFontSize(String text) {
        return 1.0f;
    }

    protected void processTextTableBeforeAddText(Table table) {
        table.add().growY().row();
    }

    protected void processOptionTextLabel(MyWrappedLabel label) {
    }

    protected void processResponseTextLabel(MyWrappedLabel label) {
    }

    protected FontConfig getImgStackTextFontConfig(float fontSize) {
        return new FontConfig(FontColor.WHITE.getColor(), FontColor.BLACK.getColor(),
                Math.round(fontSize), 3f);
    }

    protected float getVerifyBtnX() {
        return ScreenDimensionsManager.getScreenWidth() / 2 - verifyBtn.getWidth() / 2;
    }

    protected float getVerifyBtnY() {
        return ScreenDimensionsManager.getScreenHeightValue(25);
    }

    protected float getOptionWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(12);
    }

    protected float getOptionHeight() {
        return ScreenDimensionsManager.getScreenWidthValue(12);
    }

    protected float getResponseWidth() {
        return getOptionWidth();
    }

    protected float getResponseHeight() {
        return getOptionHeight();
    }

    protected float getAcceptedDistanceForDropWidth() {
        return getResponseWidth() / 3;
    }

    protected float getAcceptedDistanceForDropHeight() {
        return getResponseHeight() / 3;
    }

    public List<KidLearnImgInfo> getAlreadyMovedOptionImg() {
        sortAlreadyMovedOptionImg();
        return alreadyMovedOptionImg;
    }

}
