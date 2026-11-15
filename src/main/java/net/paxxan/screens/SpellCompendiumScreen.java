package net.paxxan.screens;

import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.paxxan.Symphony;
import net.paxxan.SymphonyClient;
import net.paxxan.spells.BaseFluteSpell;
import net.paxxan.spells.FluteSpells;
import net.paxxan.spells.PlayerSpellManager;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static net.paxxan.SymphonyClient.GALACTIC;

public class SpellCompendiumScreen extends Screen {
    public static final Identifier BOOK_LOCATION = Symphony.id("textures/gui/compendium.png");
    private static final Style PAGE_TITLE_STYLE = Style.EMPTY.withoutShadow().withColor(0xff918265).withBold(true);
    private static final Style OBSCURED_TITLE_STYLE = GALACTIC.withoutShadow().withColor(0xff918265).withBold(true);

    private static final Style PAGE_TEXT_STYLE = Style.EMPTY.withoutShadow().withColor(0xffb09f7b);
    private int currentPage;
    private static final Component TITLE = Component.translatable("symphony.book.view.title");
    private List<FormattedCharSequence> cachedPageTitles = Collections.emptyList();
    private List<FormattedCharSequence> cachedObscuredPageTitles = Collections.emptyList();
    private List<FormattedCharSequence> cachedPageTexts = Collections.emptyList();
    private List<FormattedCharSequence> cachedPageSequences = Collections.emptyList();
    private int cachedPage = -1;
    private Component pageMsg = CommonComponents.EMPTY;
    private PageButton forwardButton;
    private PageButton backButton;
    private final SpellCompendiumScreen.SpellAccess spellAccess = new SpellAccess();
    private Player player;

    public SpellCompendiumScreen(Component title, Player player) {
        super(title);
        this.player = player;
    }

    @Override
    protected void init() {
        this.createMenuControls();
        this.createPageControlButtons();
    }

    protected void createMenuControls() {
        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).pos((this.width - 200) / 2, this.menuControlsTop()).width(200).build()
        );
    }

    private static String convertSequenceToArrows(String sequence) {
        return sequence
                .replace("U", "⮝")
                .replace("L", "⮜")
                .replace("R", "⮞")
                .replace("D", "⮟");
    }


    protected void createPageControlButtons() {
        int left = this.backgroundLeft();
        int top = this.backgroundTop();
        this.forwardButton = this.addRenderableWidget(new PageButton(left + 116, top + 157, true, button -> this.pageForward(), true));
        this.backButton = this.addRenderableWidget(new PageButton(left + 43, top + 157, false, button -> this.pageBack(), true));
        this.updateButtonVisibility();
    }

    @Override
    public boolean keyPressed(final KeyEvent event) {
        if (super.keyPressed(event)) {
            return true;
        }

        return switch (event.key()) {
            case 266 -> {
                this.backButton.onPress(event);
                yield true;
            }
            case 267 -> {
                this.forwardButton.onPress(event);
                yield true;
            }
            default -> false;
        };
    }

    protected int menuControlsTop() {
        return this.backgroundTop() + 192 + 2;
    }

    @Override
    public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
        if (event.button() == 0) {
            ActiveTextCollector.ClickableStyleFinder finder = new ActiveTextCollector.ClickableStyleFinder(this.font, (int)event.x(), (int)event.y());

            this.visitText(finder, true, PlayerSpellManager.knowsSpell(this.player, Symphony.id(spellAccess.getPage(this.currentPage).getID())));
            Style clickedStyle = finder.result();
            if (clickedStyle != null && this.handleClickEvent(clickedStyle.getClickEvent())) {
                return true;
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    protected boolean handleClickEvent(final @Nullable ClickEvent event) {
        if (event == null) {
            return false;
        }

        LocalPlayer player = Objects.requireNonNull(this.minecraft.player, "Player not available");
        switch (event) {
            case ClickEvent.ChangePage(int page):
                this.forcePage(page - 1);
                break;
            case ClickEvent.RunCommand(String command):
                this.closeContainerOnServer();
                clickCommandAction(player, command, null);
                break;
            default:
                defaultHandleGameClickEvent(event, this.minecraft, this);
        }

        return true;
    }

    protected void closeContainerOnServer() {
    }

    protected void pageBack() {
        if (this.currentPage > 0) {
            this.currentPage--;
        }

        this.updateButtonVisibility();
    }
    protected boolean forcePage(final int page) {
        return this.setPage(page);
    }


    protected void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            this.currentPage++;
        }

        this.updateButtonVisibility();
    }

    @Override
    public boolean isInGameUi() {
        return true;
    }

    private void updateButtonVisibility() {
        this.forwardButton.visible = this.currentPage < this.getNumPages() - 1;
        this.backButton.visible = this.currentPage > 0;
    }

    @Override
    public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BOOK_LOCATION, this.backgroundLeft(), this.backgroundTop(), 0.0F, 0.0F, 192, 192, 256, 256);
    }


    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        boolean known = PlayerSpellManager.knowsSpell(this.player, Symphony.id(spellAccess.getPage(this.currentPage).getID()));
        this.visitText(graphics.textRenderer(GuiGraphicsExtractor.HoveredTextEffects.TOOLTIP_AND_CURSOR), false, known);
        graphics.blit(RenderPipelines.GUI_TEXTURED, known ? spellAccess.getSpellbookImage(this.currentPage) : Symphony.id("textures/item/spellbook/2d/sepia/unknown_book.png"), this.backgroundLeft() + 34, this.backgroundTop() + 28, 0.0F, 0.0F, 32, 32, 32, 32);
    }

    private void visitText(final ActiveTextCollector collector, final boolean clickableOnly, boolean known) {
        FormattedText pageTitle = ComponentUtils.mergeStyles(this.spellAccess.getPage(this.currentPage).getTranslatableName(), PAGE_TITLE_STYLE);
        FormattedText obscuredPageTitle = ComponentUtils.mergeStyles(this.spellAccess.getPage(this.currentPage).getTranslatableName(), OBSCURED_TITLE_STYLE);

        FormattedText pageBody = ComponentUtils.mergeStyles(this.spellAccess.getPage(this.currentPage).getDescription(), PAGE_TEXT_STYLE);
        FormattedText pageSequence = ComponentUtils.mergeStyles(Component.literal(convertSequenceToArrows(this.spellAccess.getPage(this.currentPage).getSequence())), PAGE_TEXT_STYLE);

        this.cachedPageTitles = this.font.split(pageTitle, 114 - 34);
        this.cachedObscuredPageTitles = this.font.split(obscuredPageTitle, 114 - 34);
        this.cachedPageTexts = this.font.split(pageBody, 114);
        this.cachedPageSequences = this.font.split(pageSequence, 114);
        this.pageMsg = ComponentUtils.mergeStyles(this.getPageNumberMessage(), PAGE_TEXT_STYLE);
        this.cachedPage = this.currentPage;



        int left = this.backgroundLeft();
        int top = this.backgroundTop();
        if (!clickableOnly) {
            collector.accept(TextAlignment.RIGHT, left + 148, top + 16, this.pageMsg);
        }

        int shownLines = Math.min(128 / 9, this.cachedPageTexts.size());
        int shownTitleLines = Math.min(128 / 9, this.cachedPageTitles.size());
        int shownSequenceLines = Math.min(128 / 9, this.cachedPageSequences.size());

        if (known) {
            for (int i = 0; i < shownTitleLines; i++) {
                FormattedCharSequence component = this.cachedPageTitles.get(i);
                collector.accept(left + 70, top + 30 + i * 9, component);

            }
            int h = 0;
            for (int j = 0; j < shownLines; j++) {
                FormattedCharSequence component = this.cachedPageTexts.get(j);
                collector.accept(left + 36, top + 74 + (j * 9), component);
                h = j + 2;
            }
            for (int k = 0; k < shownSequenceLines; k++) {
                FormattedCharSequence component = this.cachedPageSequences.get(k);
                collector.accept(left + 36, top + 74 + (h * 9) + (k * 9), component);
            }
        } else {
            for (int i = 0; i < shownTitleLines; i++) {
                FormattedCharSequence component = this.cachedObscuredPageTitles.get(i);
                collector.accept(left + 70, top + 30 + i * 9, component);
            }
        }
    }

    private Component getPageNumberMessage() {
        return Component.translatable("symphony.book.pageIndicator", this.currentPage + 1, Math.max(this.getNumPages(), 1)).withStyle(PAGE_TEXT_STYLE);
    }

    private int getNumPages() {
        return this.spellAccess.getPageCount();
    }

    public boolean setPage(final int page) {
        int clampedPage = Mth.clamp(page, 0, this.spellAccess.getPageCount() - 1);
        if (clampedPage != this.currentPage) {
            this.currentPage = clampedPage;
            this.cachedPage = -1;
            return true;
        } else {
            return false;
        }
    }

    private int backgroundLeft() {
        return (this.width - 192) / 2;
    }

    private int backgroundTop() {
        return (this.height - 192) / 2;
    }

    public record SpellAccess() {
        public static List<BaseFluteSpell> spells = getAllSpells();

        private static List<BaseFluteSpell> getAllSpells() {
            List<BaseFluteSpell> spellList = new java.util.ArrayList<>(List.of());
            spellList.addAll(FluteSpells.ALL_SPELLS);
            return spellList;
        }

        public int getPageCount() {
            return spells.size();
        }

        public BaseFluteSpell getPage(final int page) {
            return page >= 0 && page < this.getPageCount() ? spells.get(page) : new BaseFluteSpell();
        }

        public Identifier getSpellbookImage(int page) {
            Identifier texture = Symphony.id("textures/item/spellbook/2d/unknown_book.png");
            for (BaseFluteSpell spell : FluteSpells.ALL_SPELLS) {
                if (Objects.equals(spell.getID(), getPage(page).getID())) {
                    texture = spell.getSepiaBookTexture();
                    return texture;
                }
            }
            return Symphony.id("textures/item/spellbook/2d/sepia/unknown_book.png");
        }

        public static SpellCompendiumScreen.@Nullable SpellAccess fromItem(final ItemStack itemStack) {
            return spells != null ? new SpellCompendiumScreen.SpellAccess() : null;
        }
    }
}
