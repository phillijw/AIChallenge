@echo off
playgame.py --engine_seed 42 --player_seed 42 --food none --end_wait=0.25 --verbose --log_dir game_logs --turns 30 --map_file "%~dp0maps\maze\maze_p02_01.map" %1 "java -jar submission_test/MyBot.jar" -e --nolaunch --strict --capture_errors
