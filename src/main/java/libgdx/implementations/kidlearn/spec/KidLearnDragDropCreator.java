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

import libgdx.controls.ScreenRunnable;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public abstract class KidLearnDragDropCreator extends KidLearnGameCreator {

    private static final float OPT_MOVE_DURATION = 0.2f;
    private static final float UNK_FADE_DURATION = 0.1f;
    public static final float SCALE = 0.3f;
    public static final float SCALE_DURATION = 0.3f;
    public static final String SCALED_MARKER = "scaled";
    private boolean allowMultipleItemsPerResponse;
    private MyButton verifyBtn;
    protected List<KidLearnImgInfo> unknownImg = new ArrayList<>();
    protected List<KidLearnImgInfo> optionsImg = new ArrayList<>();
    protected List<KidLearnImgInfo> alreadyFilledUnknownImg = new ArrayList<>();
    protected List<KidLearnImgInfo> alreadyMovedOptionImg = new ArrayList<>();


    public KidLearnDragDropCreator(KidLearnGameContext gameContext,
                                   boolean allowMultipleItemsPerResponse) {
        super(gameContext);
        this.allowMultipleItemsPerResponse = allowMultipleItemsPerResponse;
    }

    protected abstract boolean isResponseCorrect();

    protected abstract int getTotalOptions();

    protected abstract List<String> getAllOptions();

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

    protected Stack addOptionImg(Pair<Float, Float> coord, Res res, String text) {
        Stack img = addImg(coord, res, getOptionWidth(), text);
        img.setWidth(getOptionWidth());
        img.setHeight(getOptionHeight());
        return img;
    }

    protected Stack addResponseImg(Pair<Float, Float> coord, Res res, String text) {
        Stack img = addImg(coord, res, getResponseWidth(), text);
        img.setWidth(getResponseWidth());
        img.setHeight(getResponseHeight());
        return img;
    }

    private Stack addImg(Pair<Float, Float> coord, Res res, float labelWidth, String text) {
        Stack img = createImgTextStack(text, labelWidth, res);
        img.setX(coord.getLeft());
        img.setY(coord.getRight());
        addActorToScreen(img);
        return img;
    }

    protected void createResetBtn() {
        ButtonSize btnSize = SkelClassicButtonSize.KIDLEARN_RESET;
        MyButton btn = new ButtonBuilder("Reset")
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
        }
        for (KidLearnImgInfo learnImgInfo : optionsImg) {
            Pair<Float, Float> initialCoord = learnImgInfo.initialCoord;
            learnImgInfo.img.addAction(Actions.moveTo(initialCoord.getLeft(), initialCoord.getRight(), OPT_MOVE_DURATION));
            learnImgInfo.img.setTouchable(Touchable.enabled);
            if (SCALED_MARKER.equals(learnImgInfo.img.getUserObject())) {
                learnImgInfo.img.addAction(Actions.scaleBy(SCALE, SCALE, SCALE_DURATION));
                learnImgInfo.img.setUserObject(null);
            }
        }
    }

    private void createOptionsContainer() {
        List<String> allOptions = new ArrayList<>(getAllOptions());
        Collections.shuffle(allOptions);
        for (String option : allOptions) {
            int itemsAlreadyAdded = optionsImg.size();
            Pair<Float, Float> coord = getCoordsForOptionRow(itemsAlreadyAdded);
            Stack img = addOptionImg(coord, getOptionRes(), option);
            KidLearnImgInfo opt = new KidLearnImgInfo(coord, img, String.valueOf(option));
            optionsImg.add(opt);
            img.addListener(new DragListener() {
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
                        Stack unk = unkInfo.img;
                        if ((unk.getX() - acceptedDistWidth < img.getX() && unk.getX() + responseSideDimenWidth + acceptedDistWidth > (img.getX() + optionWidth))
                                &&
                                (unk.getY() - acceptedDistHeight < img.getY() && unk.getY() + responseSideDimenHeight + acceptedDistHeight > (img.getY() + optionHeight))
                        ) {
                            if (!allowMultipleItemsPerResponse && alreadyFilledUnknownImg.contains(unkInfo)) {
                                break;
                            }
                            float moveToY = allowMultipleItemsPerResponse ? unk.getY() + unk.getHeight() / 3
                                    : unk.getY();
                            opt.img.addAction(Actions.moveTo(unk.getX(), moveToY, 0.3f));
                            if (allowMultipleItemsPerResponse && !alreadyMovedOptionImg.isEmpty()) {
                                Stack lastAddedImg = getLastAddedImgInContainer(unk.getX());
                                if (lastAddedImg != null) {
                                    opt.img.addAction(Actions.moveTo(lastAddedImg.getX(),
                                            lastAddedImg.getY() - lastAddedImg.getHeight() / 2, 0.3f));
                                }
                            }
                            noMatch = false;
                            unk.addAction(Actions.sequence(getActionsToExecuteForResponseAfterCorrect()));
                            opt.img.setTouchable(Touchable.disabled);
                            alreadyMovedOptionImg.add(opt);
                            alreadyFilledUnknownImg.add(unkInfo);

                            if (allowMultipleItemsPerResponse && !alreadyMovedOptionImg.isEmpty()) {
                                for (KidLearnImgInfo info : alreadyMovedOptionImg) {
                                    Stack stack = info.img;
                                    if (!SCALED_MARKER.equals(stack.getUserObject())) {
                                        stack.setTransform(true);
                                        stack.addAction(Actions.scaleBy(-SCALE, -SCALE, SCALE_DURATION));
                                        stack.setUserObject(SCALED_MARKER);
                                    }
                                }
                            }
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

    protected Res getOptionRes() {
        return MainResource.heart_full;
    }

    protected Action[] getActionsToExecuteForResponseAfterCorrect() {
        return new AlphaAction[]{Actions.fadeOut(UNK_FADE_DURATION)};
    }

    private Stack getLastAddedImgInContainer(float containerX) {
        Stack res = null;
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
            verifyBtn = new ButtonBuilder("Verify")
                    .setDefaultButton()
                    .setFixedButtonSize(btnSize)
                    .build();
            verifyBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!isResponseCorrect()) {
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

    private Stack createImgTextStack(String text, float labelWidth, Res res) {
        Stack stack = new Stack();
        stack.add(GraphicUtils.getImage(res));
        int standardFontSize = Math.round(FontConfig.FONT_SIZE / 1.0f);
        int fontSize = Math.round(StringUtils.isNotBlank(text) && text.length() > 3 ? standardFontSize / 1.05f :
                standardFontSize);
        fontSize = Math.round(StringUtils.isNotBlank(text) && text.length() > 4 ? standardFontSize / 1.125f :
                fontSize);
        fontSize = Math.round(StringUtils.isNotBlank(text) && text.length() > 5 ? standardFontSize / 1.2f :
                fontSize);
        MyWrappedLabel textLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setWidth(labelWidth)
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        fontSize, 5f)).setText(text).build());
        stack.add(textLabel);
        textLabel.toFront();
        return stack;
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
        return getResponseWidth() / 4;
    }

    protected float getAcceptedDistanceForDropHeight() {
        return getResponseHeight() / 4;
    }

    public List<KidLearnImgInfo> getAlreadyMovedOptionImg() {
        sortAlreadyMovedOptionImg();
        return alreadyMovedOptionImg;
    }

}
