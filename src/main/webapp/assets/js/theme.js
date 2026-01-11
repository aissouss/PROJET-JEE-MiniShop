/**
 * MiniShop - Theme Manager
 * Handles dark/light theme switching with localStorage persistence
 */

(function() {
    'use strict';

    const THEME_KEY = 'minishop_theme';
    const THEME_DARK = 'theme-dark';
    const THEME_LIGHT = 'theme-light';

    /**
     * Get current theme from localStorage or system preference
     */
    function getCurrentTheme() {
        // Check localStorage first
        const savedTheme = localStorage.getItem(THEME_KEY);
        if (savedTheme) {
            return savedTheme;
        }

        // Fallback to system preference
        if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
            return THEME_DARK;
        }

        return THEME_LIGHT;
    }

    /**
     * Apply theme to document
     */
    function applyTheme(theme) {
        const body = document.body;

        // Remove both theme classes first
        body.classList.remove(THEME_DARK, THEME_LIGHT);

        // Add the selected theme class
        if (theme === THEME_DARK || theme === THEME_LIGHT) {
            body.classList.add(theme);
        } else {
            // Default to light theme if invalid
            body.classList.add(THEME_LIGHT);
        }

        // Update theme toggle button if it exists
        updateThemeToggleButton(theme);
    }

    /**
     * Save theme to localStorage
     */
    function saveTheme(theme) {
        try {
            localStorage.setItem(THEME_KEY, theme);
            console.log('Theme saved:', theme);
        } catch (e) {
            console.error('Failed to save theme to localStorage:', e);
        }
    }

    /**
     * Toggle between dark and light theme
     */
    function toggleTheme() {
        const currentTheme = getCurrentTheme();
        const newTheme = currentTheme === THEME_DARK ? THEME_LIGHT : THEME_DARK;

        setTheme(newTheme);
    }

    /**
     * Set theme and save to localStorage
     */
    function setTheme(theme) {
        applyTheme(theme);
        saveTheme(theme);
    }

    /**
     * Update theme toggle button icon/text
     */
    function updateThemeToggleButton(theme) {
        const toggleButton = document.getElementById('theme-toggle');
        if (!toggleButton) return;

        const icon = toggleButton.querySelector('i');
        if (!icon) return;

        // Update icon based on theme
        if (theme === THEME_DARK) {
            icon.className = 'bi bi-sun-fill';
            toggleButton.setAttribute('title', 'Passer au mode clair');
        } else {
            icon.className = 'bi bi-moon-fill';
            toggleButton.setAttribute('title', 'Passer au mode sombre');
        }
    }

    /**
     * Initialize theme on page load
     */
    function initTheme() {
        const theme = getCurrentTheme();
        applyTheme(theme);

        // Add event listener to theme toggle button if it exists
        const toggleButton = document.getElementById('theme-toggle');
        if (toggleButton) {
            toggleButton.addEventListener('click', function(e) {
                e.preventDefault();
                toggleTheme();
            });
        }

        console.log('Theme initialized:', theme);
    }

    // Initialize theme as soon as possible (before DOM ready)
    initTheme();

    // Also initialize on DOM ready to ensure toggle button is set up
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initTheme);
    }

    // Export functions to window for external access
    window.MiniShopTheme = {
        toggle: toggleTheme,
        set: setTheme,
        get: getCurrentTheme,
        DARK: THEME_DARK,
        LIGHT: THEME_LIGHT
    };

})();
