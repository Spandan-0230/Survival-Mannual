import sys

with open("app/src/main/java/com/example/data/CalamityRepository.kt", "r") as f:
    content = f.read()

new_calamities = """        ),
        Calamity(
            id = "wildfire",
            name = "Wildfire",
            iconName = "warning",
            steps = listOf(
                "Evacuate immediately if instructed to do so by local authorities. Follow designated evacuation routes.",
                "If trapped, find a body of water or a cleared area with no vegetation. Lie flat and cover your body with wet clothing or soil.",
                "Close all windows and doors to prevent drafts and reduce the entry of smoke and embers.",
                "Remove flammable materials (like curtains or outdoor furniture) away from the house if time permits.",
                "Protect your lungs by breathing through a damp cloth, or use an N95 mask to filter out dangerous smoke and ash particles."
            ),
            firstAidGuide = listOf(
                "Move the victim to a safe area with fresh air immediately. Smoke inhalation can be fatal.",
                "Assess for and treat severe burn injuries. Cool burns with clean, cool water for at least 10 minutes. Do NOT use ice.",
                "Cover burns loosely with a sterile, non-fluffy dressing or clean plastic wrap. Do not apply creams or ointments.",
                "Monitor for breathing difficulties, coughing, or wheezing. Provide oxygen if available and trained to do so.",
                "Keep the person calm and treat for shock while waiting for emergency medical services."
            ),
            emergencyContacts = listOf(
                "Fire Department: 101",
                "Ambulance: 102",
                "Forest Department Helpline: 1926"
            ),
            helplineNo = "101"
        ),
        Calamity(
            id = "avalanche",
            name = "Avalanche",
            iconName = "warning",
            steps = listOf(
                "If caught in an avalanche, try to move to the side of the slide. Use a swimming motion to stay near the surface of the snow.",
                "As the snow slows down, cup your hands over your mouth and nose to create an air pocket.",
                "Keep calm and conserve oxygen. Yell for help only when you hear rescuers above you, as snow muffles sound.",
                "If you witness an avalanche, do not enter the danger zone. Mark where you last saw the victims and call for help.",
                "Use avalanche beacons, probes, and shovels to locate and dig out victims as quickly as possible."
            ),
            firstAidGuide = listOf(
                "Clear the victim's airway of snow and ice immediately. Begin CPR if they are not breathing.",
                "Treat for severe hypothermia. Move the victim to a warm environment and replace wet clothing with dry blankets.",
                "Handle the victim gently, as rough movement can cause cardiac arrest in severely hypothermic individuals.",
                "Assess for traumatic injuries, such as broken bones or spinal damage, caused by being tumbled in the snow.",
                "Apply warm packs to the neck, armpits, and groin, but avoid directly heating the extremities to prevent shock."
            ),
            emergencyContacts = listOf(
                "Mountain Rescue: 112",
                "Local Ambulance: 102"
            ),
            helplineNo = "112"
        ),
        Calamity(
            id = "volcanic_eruption",
            name = "Volcanic Eruption",
            iconName = "warning",
            steps = listOf(
                "Follow evacuation orders issued by local authorities immediately. Avoid areas downwind and river valleys downstream of the volcano.",
                "Protect yourself from falling ash by wearing long-sleeved shirts, long pants, goggles, and an N95 mask.",
                "Stay indoors with windows, doors, and ventilation systems closed until the ash settles.",
                "If driving, keep car windows rolled up and avoid driving in heavy ash fall, as it can clog the engine and obscure vision.",
                "Avoid roofs as heavy ash accumulation can cause buildings to collapse. Clear ash carefully if safe to do so."
            ),
            firstAidGuide = listOf(
                "Treat respiratory irritation by moving the person to a clear environment. Provide oxygen if they have extreme difficulty breathing.",
                "Flush eyes with generous amounts of clean water if irritated by volcanic ash. Do not rub the eyes, as ash particles are sharp and abrasive.",
                "Treat burns from hot ash or lava by cooling the area with clean water. Cover with sterile dressings.",
                "Monitor for signs of shock or dehydration, especially if the eruption is accompanied by extreme heat.",
                "Clean any open wounds meticulously, as volcanic ash can cause severe infection and tissue damage."
            ),
            emergencyContacts = listOf(
                "Disaster Management: 1078",
                "Medical Emergency: 102"
            ),
            helplineNo = "1078"
        )
"""
content = content.replace("        )\n    )\n}", new_calamities + "    )\n}")
with open("app/src/main/java/com/example/data/CalamityRepository.kt", "w") as f:
    f.write(content)
