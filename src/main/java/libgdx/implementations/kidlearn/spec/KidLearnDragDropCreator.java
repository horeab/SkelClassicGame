package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
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
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.SoundUtils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public abstract class KidLearnDragDropCreator extends KidLearnGameCreator {

    private static final float OPT_MOVE_DURATION = 0.2f;
    private static final float UNK_FADE_DURATION = 0.1f;
    private MyButton verifyBtn;
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
    }

    protected Table addOptionImg(Pair<Float, Float> coord, Res res, String text) {
        String labelName = "OPTION_LABEL" + text;
        Table img = addImg(coord, res, getOptionWidth(), labelName, text);
        processOptionTextLabel(img.findActor(labelName));
        img.setWidth(getOptionWidth());
        img.setHeight(getOptionHeight());
        return img;
    }

    protected Table addResponseImg(Pair<Float, Float> coord, Res res, String text) {
        String labelName = "RESPONSE_LABEL" + text;
        Table img = addImg(coord, res, getResponseWidth(), labelName, text);
        processResponseTextLabel(img.findActor(labelName));
        img.setWidth(getResponseWidth());
        img.setHeight(getResponseHeight());
        return img;
    }

    private Table addImg(Pair<Float, Float> coord, Res res, float labelWidth, String labelName, String text) {
        Stack img = createImgTextStack(text, labelWidth, labelName, res);
        Table table = new Table();
        table.add(img);
        table.setX(coord.getLeft());
        table.setY(coord.getRight());
        addActorToScreen(table);
        return table;
    }

    protected void createResetBtn() {
        ButtonSize btnSize = SkelClassicButtonSize.KIDLEARN_RESET;
        MyButton btn = new ButtonBuilder(KidLearnGameLabel.l_reset.getText())
                .setDefaultButton()
                .setFixedButtonSize(btnSize)
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
            Pair<Float, Float> coord = getCoordsForOptionRow(itemsAlreadyAdded);
            Table img = addOptionImg(coord, option.getRight(), option.getLeft());
            KidLearnImgInfo opt = new KidLearnImgInfo(coord, img, option.getLeft());
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
                            unk.addAction(Actions.sequence(getActionsToExecuteForResponseAfterDragStop()));
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
    }


    protected Action[] getActionsToExecuteForResponseAfterDragStop() {
        return new AlphaAction[]{Actions.fadeOut(UNK_FADE_DURATION)};
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
                    .setDefaultButton()
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
            verifyBtn.setX(ScreenDimensionsManager.getScreenWidth() / 2 - verifyBtn.getWidth() / 2);
            verifyBtn.setY(getVerifyBtnY());
            addActorToScreen(verifyBtn);
        } else {
            verifyBtn.setVisible(true);
        }
        verifyBtn.toFront();
    }

    private Stack createImgTextStack(String text, float labelWidth, String labelName, Res res) {
        Stack stack = new Stack();
        stack.add(GraphicUtils.getImage(res));
        MyWrappedLabel textLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setWidth(labelWidth)
                .setFontConfig(getImgStackTextFontConfig(text)).setText(text).build());
        textLabel.setName(labelName);
        stack.add(textLabel);
        textLabel.toFront();
        return stack;
    }

    protected int getOptionFontSize(String text) {
        int standardFontSize = Math.round(FontConfig.FONT_SIZE / 1.0f);
        int fontSize = Math.round(StringUtils.isNotBlank(text) && text.length() > 3 ? standardFontSize / 1.05f :
                standardFontSize);
        fontSize = Math.round(StringUtils.isNotBlank(text) && text.length() > 4 ? standardFontSize / 1.125f :
                fontSize);
        fontSize = Math.round(StringUtils.isNotBlank(text) && text.length() > 5 ? standardFontSize / 1.2f :
                fontSize);
        return fontSize;
    }

    protected void processOptionTextLabel(MyWrappedLabel label) {
    }

    protected void processResponseTextLabel(MyWrappedLabel label) {
    }

    protected FontConfig getImgStackTextFontConfig(String text) {
        return new FontConfig(FontColor.WHITE.getColor(), FontColor.BLACK.getColor(),
                getOptionFontSize(text), 3f);
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
