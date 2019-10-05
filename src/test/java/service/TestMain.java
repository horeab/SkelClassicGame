package service;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import libgdx.constants.GameIdEnum;
import libgdx.controls.popup.RatingService;
import libgdx.game.Game;
import libgdx.implementations.skelgame.IqGame;
import libgdx.game.external.AppInfoService;
import libgdx.preferences.PreferencesService;
import libgdx.preferences.SettingsService;
import libgdx.screen.AbstractScreen;
import libgdx.utils.DateUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.startgame.test.DefaultAppInfoService;
import libgdx.utils.startgame.test.DefaultBillingService;
import libgdx.utils.startgame.test.DefaultFacebookService;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SpriteBatch.class, RatingService.class, ScreenDimensionsManager.class, SettingsService.class, AbstractScreen.class})
public class TestMain implements ApplicationListener {

    protected PreferencesService preferencesService;
    protected Preferences preferences;

    @Before
    public void setup() throws Exception {
        createMocks();
        Game.getInstance().getAssetManager().finishLoading();
    }

    private void createMocks() throws Exception {
        HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
        new HeadlessApplication(this, conf);
        Gdx.gl = PowerMockito.mock(GL20.class);
        Gdx.gl20 = PowerMockito.mock(GL20.class);

        ShaderProgram shaderProgram = Mockito.mock(ShaderProgram.class);
        PowerMockito.when(shaderProgram.isCompiled()).thenReturn(true);
        PowerMockito.mockStatic(SpriteBatch.class);
        PowerMockito.when(SpriteBatch.createDefaultShader()).thenReturn(shaderProgram);

        Gdx.graphics = PowerMockito.mock(Graphics.class);
        PowerMockito.when(Gdx.graphics.getHeight()).thenReturn(853);
        PowerMockito.when(Gdx.graphics.getWidth()).thenReturn(480);

        preferences = Mockito.mock(Preferences.class);
        preferencesService = Mockito.mock(PreferencesService.class);
        PowerMockito.when(preferencesService.getPreferences()).thenReturn(preferences);
        PowerMockito.whenNew(PreferencesService.class).withAnyArguments().thenReturn(preferencesService);
        Mockito.when(preferencesService.getPreferences().getInteger("TransactionAmountEnum_VERSION")).thenReturn(0);
        Mockito.when(preferencesService.getPreferences().getString("TransactionAmountEnum_VALUE")).thenReturn("");

        Game game = new Game(new DefaultFacebookService(), new DefaultBillingService(), new DefaultAppInfoService());
        game.create();

        Game.getInstance().setScreen(null);
    }

    public static void assertDateTimeNow(String date) {
        assertDateTimeNow(DateUtils.getDate(date).getTime());
    }

    public static void assertDateTimeNow(Long date) {
        Date dateTime = new Date(date);
        Calendar instance1 = Calendar.getInstance();
        instance1.setTimeInMillis(new Date().getTime());
        Calendar instance2 = Calendar.getInstance();
        instance2.setTimeInMillis(dateTime.getTime());
        assertEquals(instance1.get(Calendar.YEAR), instance2.get(Calendar.YEAR));
        assertEquals(instance1.get(Calendar.MONTH), instance2.get(Calendar.MONTH));
        assertEquals(instance1.get(Calendar.DAY_OF_MONTH), instance2.get(Calendar.DAY_OF_MONTH));
        assertEquals(instance1.get(Calendar.HOUR_OF_DAY), instance2.get(Calendar.HOUR_OF_DAY));
        // (until test runs it takes maximum 30 seconds - BUFFER) expected is greater
        // than db Value, but lesser than db Value - 30 seconds
        assertTrue((new Date().after(dateTime) || new Date().compareTo(dateTime) == 0) && DateUtils.minusSeconds(30).before(dateTime));
    }

    @Override
    public void create() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void render() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void dispose() {
    }

}
